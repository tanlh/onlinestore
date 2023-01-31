package com.onlinestore.product.core.data;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
public class ProductEntity {

    @Id
    String productId;

    @Column(unique = true)
    String title;

    BigDecimal price;

    int quantity;

}
