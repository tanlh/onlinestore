package com.onlinestore.product.query.rest;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {

    String id;
    String title;
    BigDecimal price;
    int quantity;

}
