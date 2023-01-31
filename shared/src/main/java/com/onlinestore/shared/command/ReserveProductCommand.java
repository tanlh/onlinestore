package com.onlinestore.shared.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public record ReserveProductCommand(
    @TargetAggregateIdentifier String productId,
    @NotBlank String orderId,
    @NotBlank String userId,
    @Positive int quantity
) {}
