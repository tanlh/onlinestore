package com.onlinestore.order.command.aggregate;

import com.onlinestore.order.command.commands.ApproveOrderCommand;
import com.onlinestore.order.command.commands.CreateOrderCommand;
import com.onlinestore.order.command.commands.RejectOrderCommand;
import com.onlinestore.order.core.data.OrderStatus;
import com.onlinestore.order.core.events.OrderApprovedEvent;
import com.onlinestore.order.core.events.OrderCreatedEvent;
import com.onlinestore.order.core.events.OrderRejectedEvent;
import com.onlinestore.order.mapper.OrderMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@Data
@NoArgsConstructor
public class OrderAggregate {

    @AggregateIdentifier
    String orderId;
    String productId;
    String userId;
    int quantity;
    String addressId;
    OrderStatus orderStatus;

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command, OrderMapper mapper) {
        AggregateLifecycle.apply(mapper.toOrderCreatedEvent(command));

//        if (new Random().nextBoolean()) {
//            // This error will be wrapped by CommandExecutionException
//            throw new RuntimeException("Order aggregate throw error");
//        }
    }

    @CommandHandler
    public void handle(ApproveOrderCommand command) {
        AggregateLifecycle.apply(new OrderApprovedEvent(command.orderId()));
    }

    @CommandHandler
    public void handle(RejectOrderCommand command, OrderMapper mapper) {
        AggregateLifecycle.apply(mapper.toOrderRejectedEvent(command));
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        BeanUtils.copyProperties(event, this);
    }

    @EventSourcingHandler
    public void on(OrderApprovedEvent event) {
        this.orderStatus = event.getOrderStatus();
    }

    @EventSourcingHandler
    public void on(OrderRejectedEvent event) {
        this.orderStatus = event.getOrderStatus();
    }

}
