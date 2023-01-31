package com.onlinestore.order.command.rest;

import com.onlinestore.order.command.commands.CreateOrderCommand;
import com.onlinestore.order.core.data.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderCommandController {

    final CommandGateway commandGateway;

    @PostMapping
    public String createOrder(@Valid @RequestBody CreateOrderRequest request) {
        var createOrderCommand = CreateOrderCommand.builder()
            .orderId(UUID.randomUUID().toString())
            .userId("27b95829-4f3f-4ddf-8983-151ba010e35b")
            .productId(request.getProductId())
            .addressId(request.getAddressId())
            .quantity(request.getQuantity())
            .orderStatus(OrderStatus.CREATED)
            .build();

        return commandGateway.sendAndWait(createOrderCommand);
    }

}
