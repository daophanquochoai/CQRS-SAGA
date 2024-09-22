package com.doctorhoai.productservice.query.rest;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductRestModel implements Serializable {
    private static final long serialVersion = 123123897123L;
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
