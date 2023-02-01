package com.onlinestore.order.query.handler;

import com.onlinestore.order.core.data.OrderRepository;
import com.onlinestore.order.core.model.OrderSummary;
import com.onlinestore.order.mapper.OrderMapper;
import com.onlinestore.order.query.queries.FindOrderQuery;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class OrderQueryHandler {

    OrderRepository orderRepository;
    OrderMapper mapper;

    @QueryHandler
    public OrderSummary handle(FindOrderQuery query) {
        var order = orderRepository.findByOrderId(query.orderId());
        return OrderSummary.builder()
            .orderId(order.getOrderId())
            .orderStatus(order.getOrderStatus())
            .build();
    }

}
