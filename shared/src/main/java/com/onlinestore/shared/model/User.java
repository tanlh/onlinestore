package com.onlinestore.shared.model;

import lombok.Builder;

@Builder
public record User(
    String firstName,
    String lastName,
    String userId,
    PaymentDetails paymentDetails
) {}
