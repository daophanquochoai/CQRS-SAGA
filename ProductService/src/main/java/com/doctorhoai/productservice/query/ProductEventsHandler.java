package com.doctorhoai.productservice.query;

import com.doctorhoai.core.event.ProductReservationCancelledEvent;
import com.doctorhoai.core.event.ProductReservedEvent;
import com.doctorhoai.productservice.core.data.ProductEntity;
import com.doctorhoai.productservice.core.data.ProductRepository;
import com.doctorhoai.productservice.core.event.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
@RequiredArgsConstructor
@ProcessingGroup("product-group")
@Slf4j
public class ProductEventsHandler {

    private final ProductRepository productRepository;
    @ExceptionHandler( Exception.class)
    public void handle( Exception exception) throws Exception{
        throw exception;
    }
    @ExceptionHandler( IllegalArgumentException.class)
    public void handle( IllegalArgumentException exception) throws Exception{
        throw exception;
    }
    @EventHandler
    public void on(ProductCreatedEvent event) throws Exception {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(event, productEntity);
        try{
            productRepository.save(productEntity);
        }catch ( IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void on(ProductReservedEvent productReservedEvent){
        ProductEntity productEntity = productRepository.findByProductId(productReservedEvent.getProductId());
        productEntity.setQuantity(productEntity.getQuantity() - productReservedEvent.getQuantity());
        productRepository.save(productEntity);
        log.info("Product : ProductReservedEvent is called for productId");
    }

    @EventHandler
    public void on(ProductReservationCancelledEvent productReservationCancelledEvent){
        ProductEntity productEntity = productRepository.findByProductId(productReservationCancelledEvent.getProductId());
        productEntity.setQuantity(productEntity.getQuantity() + productReservationCancelledEvent.getQuantity());
        productRepository.save(productEntity);
    }
}
