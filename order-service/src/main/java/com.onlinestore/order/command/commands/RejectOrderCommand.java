package com.onlinestore.order.command.commands;

import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import javax.validation.constraints.NotBlank;

@Builder
public record RejectOrderCommand(
    @TargetAggregateIdentifier String orderId,
    @NotBlank String reason
) {}
