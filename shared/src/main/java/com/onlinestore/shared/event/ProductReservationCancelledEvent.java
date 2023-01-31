package com.onlinestore.shared.event;

public record ProductReservationCancelledEvent(
    String orderId,
    String productId,
    String userId,
    int quantity,
    String reason
) {}
