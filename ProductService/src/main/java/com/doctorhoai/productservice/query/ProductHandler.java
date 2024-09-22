package com.doctorhoai.productservice.query;

import com.doctorhoai.productservice.core.data.ProductEntity;
import com.doctorhoai.productservice.core.data.ProductRepository;
import com.doctorhoai.productservice.query.rest.ProductRestModel;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductRepository productRepository;
    @QueryHandler
    public List<ProductRestModel> findProduct( FindProductQuery query ){
        List<ProductRestModel> productRest = new ArrayList<>();
        List<ProductEntity> storeProducts = productRepository.findAll();
        for( ProductEntity entity : storeProducts ){
            ProductRestModel productRestModel = new ProductRestModel();
            BeanUtils.copyProperties(entity, productRestModel);
            productRest.add(productRestModel);
        }
        return productRest;
    }
}
