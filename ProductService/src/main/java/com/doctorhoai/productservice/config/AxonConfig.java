package com.doctorhoai.productservice.config;

import com.doctorhoai.core.commands.ReserveProductCommand;
import com.doctorhoai.core.event.ProductReservedEvent;
import com.doctorhoai.productservice.command.CreateProductCommand;
import com.doctorhoai.productservice.core.event.ProductCreatedEvent;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.springframework.context.annotation.Bean;
import com.thoughtworks.xstream.XStream;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AxonConfig {
    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();
        // Allow the specific class
        xStream.allowTypes(new Class[]{ReserveProductCommand.class});
        xStream.allowTypes(new Class[]{CreateProductCommand.class});
        xStream.allowTypes(new Class[]{ProductCreatedEvent.class});
        xStream.allowTypes(new Class[]{ProductReservedEvent.class});
        return xStream;
    }

    @Bean
    @Primary
    public XStreamSerializer xStreamSerializer(XStream xStream) {
        return XStreamSerializer.builder().xStream(xStream).build();
    }
}

