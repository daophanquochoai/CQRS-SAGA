package com.doctorhoai.productservice.command.controller;

import com.doctorhoai.productservice.command.CreateProductCommand;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final CommandGateway commandGateway;

    @PostMapping
    public String createProduct(
            @RequestBody CreateProductRestModel createProductRestModel
    ){
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .productId(UUID.randomUUID().toString())
                .price(createProductRestModel.getPrice())
                .quantity(createProductRestModel.getQuantity())
                .title(createProductRestModel.getTitle())
                .build();
        String returnValue;
//        try{
            returnValue = commandGateway.sendAndWait(createProductCommand);
//        }catch ( Exception ex){
//            returnValue = ex.getLocalizedMessage();
//        }
        return returnValue;
    }
//    @GetMapping
//    public String getProduct(){
//        return "HTTP GET HANDLER";
//    }
//    @PutMapping
//    public String putProduct(){
//        return "HTTP PUT HANDLER";
//    }
//    @DeleteMapping
//    public String deleteProduct(){
//        return "HTTP DELETE HANDLER";
//    }
}
