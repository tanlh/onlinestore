package com.onlinestore.order.command.rest;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class CreateOrderRequest {

    @NotBlank
    String productId;

    @NotBlank
    String addressId;

    @Positive
    int quantity;

}
