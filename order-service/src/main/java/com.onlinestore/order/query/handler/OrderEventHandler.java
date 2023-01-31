package com.onlinestore.order.query.handler;

import com.onlinestore.order.core.data.OrderRepository;
import com.onlinestore.order.core.events.OrderApprovedEvent;
import com.onlinestore.order.core.events.OrderCreatedEvent;
import com.onlinestore.order.core.events.OrderRejectedEvent;
import com.onlinestore.order.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@ProcessingGroup("order")
@Slf4j
public class OrderEventHandler {

    OrderRepository orderRepository;
    OrderMapper mapper;

    @EventHandler
    public void on(OrderCreatedEvent event) {
        orderRepository.save(mapper.toOrderEntity(event));

//        var random = new Random();
//        if (random.nextBoolean()) {
//            if (random.nextBoolean()) {
//                throw new IllegalStateException("Order event handler illegal error");
//            }
//
//            throw new RuntimeException("Order event handler runtime error");
//        }
    }

    @EventHandler
    public void on(OrderApprovedEvent event) {
        orderRepository.updateOrderStatus(event.getOrderId(), event.getOrderStatus().name());
    }

    @EventHandler
    public void on(OrderRejectedEvent event) {
        orderRepository.updateOrderStatus(event.getOrderId(), event.getOrderStatus().name());
    }

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalArgumentException ex) {
        log.error("Illegal error happens", ex);

        // rethrow to propagate exception to let axon rollback the transaction
        // but it's not enough, we have to register PropagatingExceptionHandler (type of ListenerInvocationErrorHandler) also
        throw ex;
    }

    @ExceptionHandler
    public void handle(Exception ex) throws Exception {
        log.error("Other error happens", ex);
        throw ex;
    }

}
