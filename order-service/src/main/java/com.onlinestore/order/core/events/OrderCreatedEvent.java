package com.onlinestore.order.core.events;

import com.onlinestore.order.core.data.OrderStatus;
import lombok.Data;

@Data
public class OrderCreatedEvent {

    String orderId;
    String productId;
    String userId;
    int quantity;
    String addressId;
    OrderStatus orderStatus;

}
