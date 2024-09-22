package com.doctorhoai.productservice.query.rest;

import com.doctorhoai.productservice.query.FindProductQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsQueryController {

    private final QueryGateway queryGateway;
    @GetMapping
    public List<ProductRestModel> getProducts(){
        FindProductQuery findProductQuery = new FindProductQuery();
        List<ProductRestModel> products = queryGateway.query(findProductQuery,
                ResponseTypes.multipleInstancesOf(ProductRestModel.class)
                ).join();
        return products;
    }
}
