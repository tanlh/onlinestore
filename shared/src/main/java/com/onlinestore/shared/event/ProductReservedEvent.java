package com.onlinestore.shared.event;

public record ProductReservedEvent(
    String orderId,
    String productId,
    String userId,
    int quantity
) {}
