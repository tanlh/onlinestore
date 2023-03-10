package com.onlinestore.order.mapper;

import com.onlinestore.order.command.commands.CreateOrderCommand;
import com.onlinestore.order.command.commands.RejectOrderCommand;
import com.onlinestore.order.core.data.OrderEntity;
import com.onlinestore.order.core.events.OrderApprovedEvent;
import com.onlinestore.order.core.events.OrderCreatedEvent;
import com.onlinestore.order.core.events.OrderRejectedEvent;
import com.onlinestore.order.core.model.OrderSummary;
import com.onlinestore.shared.command.CancelProductReservationCommand;
import com.onlinestore.shared.command.ReserveProductCommand;
import com.onlinestore.shared.event.ProductReservationCancelledEvent;
import com.onlinestore.shared.event.ProductReservedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderCreatedEvent toOrderCreatedEvent(CreateOrderCommand command);

    OrderEntity toOrderEntity(OrderCreatedEvent event);

    ReserveProductCommand toReserveProductCommand(OrderCreatedEvent event);

    CancelProductReservationCommand toCancelProductReservationCommand(ProductReservedEvent event, String reason);

    RejectOrderCommand toRejectOrderCommand(ProductReservationCancelledEvent event);

    OrderRejectedEvent toOrderRejectedEvent(RejectOrderCommand command);

    OrderSummary toOrderSummary(OrderEntity entity);
    OrderSummary toOrderSummary(OrderApprovedEvent event);
    @Mapping(source = "reason", target = "message")
    OrderSummary toOrderSummary(OrderRejectedEvent event);

}
