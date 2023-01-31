package com.onlinestore.product.command.rest;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class CreateProductRequest {

    @NotBlank
    String title;

    @Positive
    BigDecimal price;

    @Positive
    int quantity;

}
