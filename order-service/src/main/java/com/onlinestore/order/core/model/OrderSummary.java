package com.onlinestore.order.core.model;

import lombok.Builder;

@Builder
public record OrderSummary(
    String orderId,
    OrderStatus orderStatus,
    String message
) {}
