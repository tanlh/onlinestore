package com.onlinestore.order.core.events;

import com.onlinestore.order.core.model.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderRejectedEvent {

    String orderId;
    String reason;
    @Builder.Default
    OrderStatus orderStatus = OrderStatus.REJECTED;

}
