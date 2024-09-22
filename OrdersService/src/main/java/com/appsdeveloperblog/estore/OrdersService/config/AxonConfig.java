package com.appsdeveloperblog.estore.OrdersService.config;

import com.appsdeveloperblog.estore.OrdersService.command.commands.ApproveOrderCommand;
import com.appsdeveloperblog.estore.OrdersService.command.commands.CreateOrderCommand;
import com.appsdeveloperblog.estore.OrdersService.core.events.OrderApprovedEvent;
import com.appsdeveloperblog.estore.OrdersService.core.events.OrderCreatedEvent;
import com.appsdeveloperblog.estore.OrdersService.query.FindOrderQuery;
import com.appsdeveloperblog.estore.OrdersService.saga.OrderSaga;
import com.doctorhoai.core.commands.ReserveProductCommand;
import com.doctorhoai.core.commands.User;
import com.doctorhoai.core.event.PaymentProcessedEvent;
import com.doctorhoai.core.event.ProductReservedEvent;
import com.doctorhoai.core.query.FetchUserPaymentDetailsQuery;
import com.thoughtworks.xstream.XStream;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AxonConfig {

    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();
        // Cho phép các lớp cần thiết
        xStream.allowTypes(new Class[]{
                ReserveProductCommand.class,
                ProductReservedEvent.class,
                CreateOrderCommand.class,
                FindOrderQuery.class,
                OrderCreatedEvent.class,
                OrderSaga.class,
                FetchUserPaymentDetailsQuery.class,
                User.class,
                PaymentProcessedEvent.class,
                ApproveOrderCommand.class,
                OrderApprovedEvent.class
                // Thêm các lớp khác nếu cần
        });
        return xStream;
    }

    @Bean
    @Primary
    public XStreamSerializer xStreamSerializer(XStream xStream) {
        return XStreamSerializer.builder().xStream(xStream).build();
    }
}