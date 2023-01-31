package com.onlinestore.order.core.events;

import com.onlinestore.order.core.data.OrderStatus;
import lombok.Data;

@Data
public class OrderApprovedEvent {

    final String orderId;
    OrderStatus orderStatus = OrderStatus.APPROVED;

}
