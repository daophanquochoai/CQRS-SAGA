package com.doctorhoai.productservice.core.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Table( name = "products")
@Entity
public class ProductEntity implements Serializable {
    private static final long serialVersionUUID = -22234234234L;
    @Id
    private String productId;
//    @Column(unique = true)
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
