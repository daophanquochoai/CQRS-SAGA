package com.doctorhoai.productservice.command;

import com.doctorhoai.productservice.core.data.ProductLookupEntity;
import com.doctorhoai.productservice.core.data.ProductLookupRepository;
import com.doctorhoai.productservice.core.event.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
@RequiredArgsConstructor
public class ProductLookupEventHandler {
    private final ProductLookupRepository productLookupRepository;
    @EventHandler
    public void on(ProductCreatedEvent event){
        ProductLookupEntity productLookupEntity = new ProductLookupEntity(event.getProductId(), event.getTitle());
        productLookupRepository.save(productLookupEntity);
    }
}
