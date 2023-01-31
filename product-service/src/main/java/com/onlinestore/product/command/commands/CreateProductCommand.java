package com.onlinestore.product.command.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateProductCommand(
    @TargetAggregateIdentifier String productId,
    @NotBlank String title,
    @Positive BigDecimal price,
    @Positive int quantity
) {}
