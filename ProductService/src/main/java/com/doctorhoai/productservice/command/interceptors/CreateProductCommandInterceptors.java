package com.doctorhoai.productservice.command.interceptors;

import com.doctorhoai.productservice.command.CreateProductCommand;
import com.doctorhoai.productservice.core.data.ProductLookupEntity;
import com.doctorhoai.productservice.core.data.ProductLookupRepository;
import com.doctorhoai.productservice.core.event.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateProductCommandInterceptors implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final ProductLookupRepository productLookupRepository;
    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {
            log.info("Interceptor command : " + command.getPayloadType());
            if( CreateProductCommand.class.equals(command.getPayloadType()) ){
                CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();
//                if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0 ){
//                    throw new IllegalArgumentException("Price cannot be less or equal than zero");
//                }
//                if( createProductCommand.getTitle() == null || createProductCommand.getTitle().isBlank() ){
//                    throw new IllegalArgumentException("Title cannot be empty");
//                }
                ProductLookupEntity productLookupEntity = productLookupRepository.findByProductIdOrTitle(createProductCommand.getProductId(), createProductCommand.getTitle());
                if( productLookupEntity != null ){
                    throw new IllegalStateException(
                            String.format("Product with productId %s title %s already exist", createProductCommand.getProductId(), createProductCommand.getTitle())
                    );
                }
            }
            return command;
        };
    }
}
