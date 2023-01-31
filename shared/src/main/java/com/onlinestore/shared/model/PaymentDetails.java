package com.onlinestore.shared.model;

import lombok.Builder;

@Builder
public record PaymentDetails(
    String name,
    String cardNumber,
    int validUntilMonth,
    int validUntilYear,
    String cvv
) {}
