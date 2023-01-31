package com.onlinestore.order.command.commands;

import com.onlinestore.order.core.data.OrderStatus;
import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Builder
public record CreateOrderCommand(
    @TargetAggregateIdentifier String orderId,
    @NotBlank String userId,
    @NotBlank String productId,
    @Positive int quantity,
    @NotBlank String addressId,
    @NotNull OrderStatus orderStatus
) {}
