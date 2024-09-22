package com.appsdeveloperblog.estore.OrdersService.saga;

import com.appsdeveloperblog.estore.OrdersService.command.commands.ApproveOrderCommand;
import com.appsdeveloperblog.estore.OrdersService.command.commands.RejectOrderCommand;
import com.appsdeveloperblog.estore.OrdersService.core.events.OrderApprovedEvent;
import com.appsdeveloperblog.estore.OrdersService.core.events.OrderCreatedEvent;
import com.appsdeveloperblog.estore.OrdersService.core.events.OrderRejectedEvent;
import com.doctorhoai.core.commands.CancelProductReservationCommand;
import com.doctorhoai.core.commands.ProcessPaymentCommand;
import com.doctorhoai.core.commands.ReserveProductCommand;
import com.doctorhoai.core.commands.User;
import com.doctorhoai.core.event.PaymentProcessedEvent;
import com.doctorhoai.core.event.ProductReservationCancelledEvent;
import com.doctorhoai.core.event.ProductReservedEvent;
import com.doctorhoai.core.query.FetchUserPaymentDetailsQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


import javax.annotation.Nonnull;

@Saga
@Slf4j
@RequiredArgsConstructor
public class OrderSaga {

    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient QueryGateway queryGateway;
    @Autowired
    private transient DeadlineManager deadlineManager;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
                .orderId(orderCreatedEvent.getOrderId())
                .productId(orderCreatedEvent.getProductId())
                .quantity(orderCreatedEvent.getQuantity())
                .userId(orderCreatedEvent.getUserId())
                .build();

        log.info("Saga: OrderCreateEvent handle for orderId: {}", orderCreatedEvent.getOrderId());
        commandGateway.send(reserveProductCommand, new CommandCallback<ReserveProductCommand, Object>() {
            @Override
            public void onResult(@Nonnull CommandMessage<? extends ReserveProductCommand> commandMessage, @Nonnull CommandResultMessage<?> commandResultMessage) {
                log.info("Saga: OrderCreateEvent handle success");
                if (commandResultMessage.isExceptional()) {
                    log.error("Saga: Error during ReserveProductCommand execution");
                    // handle rollback logic here
                    return;
                } else {
                    log.info("Saga: ReserveProductCommand executed successfully for orderId: {}", orderCreatedEvent.getOrderId());
                }
            }
        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent productReservedEvent) {
        log.info("Saga: ProductReservedEvent is called for productId: {}", productReservedEvent.getProductId());
        // Continue with processing user payment here
        FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery =
                new FetchUserPaymentDetailsQuery(productReservedEvent.getUserId());
        User userPaymentDetails = null;
        try{
            userPaymentDetails = queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
        }catch ( Exception ex ){
            log.info(ex.getMessage());
            // begin transaction
            cancelProductReservation(productReservedEvent, ex.getMessage());
            return;
        }
        if( userPaymentDetails == null ){
            cancelProductReservation(productReservedEvent, "Could not fetch user payment details");
            return;
        }
        log.info("Successfully fetched user payment details for user " + userPaymentDetails.getFirstName());

        deadlineManager.schedule(Duration.of(10, ChronoUnit.SECONDS),"payment-processing-deadline", productReservedEvent);

        ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
                .orderId(productReservedEvent.getOrderId())
                .paymentId(UUID.randomUUID().toString())
                .paymentDetails(userPaymentDetails.getPaymentDetails())
                .build();
        String result = null;
        try{
            result = commandGateway.sendAndWait(processPaymentCommand, 10 , TimeUnit.SECONDS);
        }
        catch ( Exception ex ){
            log.error(ex.getMessage());
            // start compensating transaction
            cancelProductReservation(productReservedEvent, ex.getMessage());
            return;
        }
        if( result == null) {
            log.info("The ProcessPaymentCommand resulted in Null. Initiating a compensating transaction");
            // start compensating transasction
            cancelProductReservation(productReservedEvent, "Could not process user payment with provided result is null");
            return;
        }
    }

    public void cancelProductReservation( ProductReservedEvent productReservedEvent, String reason){
        CancelProductReservationCommand publishProductReservationCommand =
                CancelProductReservationCommand.builder()
                        .orderId(productReservedEvent.getOrderId())
                        .productId(productReservedEvent.getProductId())
                        .quantity(productReservedEvent.getQuantity())
                        .userId(productReservedEvent.getUserId())
                        .reason(reason)
                        .build();
        commandGateway.send(publishProductReservationCommand);
    }

    @SagaEventHandler( associationProperty = "orderId")
    public void handle(PaymentProcessedEvent paymentProcessedEvent){
        deadlineManager.cancelAll("payment-processing-deadline");
        ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(paymentProcessedEvent.getOrderId());
        try{
            commandGateway.send(approveOrderCommand);
        }
        catch ( Exception ex ){
            log.error(ex.getMessage());
            return;
        }
    }
    @EndSaga // option 1 : end saga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApprovedEvent orderApprovedEvent ){
        log.info("Saga : Order is approved");
//        SagaLifecycle.end();  option 2 : end saga
    }

    @SagaEventHandler( associationProperty = "orderId")
    public void handler(ProductReservationCancelledEvent productReservationCancelledEvent){
        RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(productReservationCancelledEvent.getOrderId(), productReservationCancelledEvent.getReason());
        commandGateway.send(rejectOrderCommand);
    }
    @EndSaga
    @SagaEventHandler( associationProperty = "orderId")
    public void handle(OrderRejectedEvent orderRejectedEvent){
        log.info("Successfully rejected order with id : " + orderRejectedEvent.getOrderId());
    }
    @DeadlineHandler(deadlineName = "payment-processing-deadling")
    public void handlePaymentDeadline( ProductReservedEvent productReservedEvent ){
        log.info("Payment processing deadline took place");
        cancelProductReservation(productReservedEvent, "Payment timeout");
    }
}

