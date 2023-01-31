package com.onlinestore.shared.event;

public record PaymentProcessedEvent(
    String orderId,
    String paymentId
) {}