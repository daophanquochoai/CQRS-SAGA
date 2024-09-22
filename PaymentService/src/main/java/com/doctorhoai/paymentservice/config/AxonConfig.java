package com.doctorhoai.paymentservice.config;

import com.doctorhoai.core.commands.ProcessPaymentCommand;
import com.doctorhoai.core.commands.ReserveProductCommand;
import com.doctorhoai.core.commands.User;
import com.doctorhoai.core.event.PaymentProcessedEvent;
import com.doctorhoai.core.event.ProductReservedEvent;
import com.doctorhoai.core.query.FetchUserPaymentDetailsQuery;
import com.thoughtworks.xstream.XStream;
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
                FetchUserPaymentDetailsQuery.class,
                User.class,
                ProcessPaymentCommand.class,
                PaymentProcessedEvent.class
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
