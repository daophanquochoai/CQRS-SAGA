package com.doctorhoai.productservice.core.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// entity tra cuu
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name = "productlookup")
public class ProductLookupEntity implements Serializable {
    private static final long serialVersion = 123198237L;
    @Id
    private String productId;
    @Column( unique = true )
    private String title;
}
