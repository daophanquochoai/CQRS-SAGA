package com.doctorhoai.paymentservice.command;

import com.doctorhoai.core.commands.ProcessPaymentCommand;
import com.doctorhoai.core.event.PaymentProcessedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class PaymentAggregate {
    @AggregateIdentifier
    private String paymentId;
    private String orderId;
    public PaymentAggregate(){}
    @CommandHandler
    public PaymentAggregate(ProcessPaymentCommand processPaymentCommand){
//        if(processPaymentCommand.getPaymentDetails() == null) {
//            throw new IllegalArgumentException("Missing payment details");
//        }
//
//        if(processPaymentCommand.getOrderId() == null) {
//            throw new IllegalArgumentException("Missing orderId");
//        }
//
//        if(processPaymentCommand.getPaymentId() == null) {
//            throw new IllegalArgumentException("Missing paymentId");
//        }

        AggregateLifecycle.apply(new PaymentProcessedEvent(processPaymentCommand.getOrderId(),
                processPaymentCommand.getPaymentId()));
    }
    @EventSourcingHandler
    public void on( PaymentProcessedEvent paymentProcessedEvent){
        this.orderId = paymentProcessedEvent.getOrderId();
        this.paymentId = paymentProcessedEvent.getPaymentId();
    }
}
