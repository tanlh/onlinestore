package com.onlinestore.order.command.rest;

import com.onlinestore.order.command.commands.CreateOrderCommand;
import com.onlinestore.order.core.model.OrderStatus;
import com.onlinestore.order.core.model.OrderSummary;
import com.onlinestore.order.query.queries.FindOrderQuery;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class OrderCommandController {

    CommandGateway commandGateway;
    QueryGateway queryGateway;

    @PostMapping
    public OrderSummary createOrder(@Valid @RequestBody CreateOrderRequest request) {
        var orderId = UUID.randomUUID().toString();
        var createOrderCommand = CreateOrderCommand.builder()
            .orderId(orderId)
            .userId("27b95829-4f3f-4ddf-8983-151ba010e35b")
            .productId(request.getProductId())
            .addressId(request.getAddressId())
            .quantity(request.getQuantity())
            .orderStatus(OrderStatus.CREATED)
            .build();

        @Cleanup var queryResult = queryGateway.subscriptionQuery(new FindOrderQuery(orderId), OrderSummary.class, OrderSummary.class);
        commandGateway.sendAndWait(createOrderCommand);

        return queryResult.updates().blockFirst();
    }

}
