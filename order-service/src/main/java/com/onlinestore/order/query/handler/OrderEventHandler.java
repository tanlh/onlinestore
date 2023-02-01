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

import javax.persistence.PersistenceException;

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
    }

    @EventHandler
    public void on(OrderApprovedEvent event) {
        orderRepository.updateOrderStatus(event.getOrderId(), event.getOrderStatus().name());
    }

    @EventHandler
    public void on(OrderRejectedEvent event) {
        orderRepository.updateOrderStatus(event.getOrderId(), event.getOrderStatus().name());
    }

    @ExceptionHandler(resultType = PersistenceException.class)
    public void handle(PersistenceException ex) {
        log.error("Error while performing data change to database", ex);
        throw ex;
    }

    @ExceptionHandler
    public void handle(Exception ex) throws Exception {
        log.error("Other error happens", ex);
        throw ex;
    }

}
