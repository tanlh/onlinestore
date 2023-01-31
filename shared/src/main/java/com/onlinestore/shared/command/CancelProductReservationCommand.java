package com.onlinestore.shared.command;

import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Builder
public record CancelProductReservationCommand(
    @TargetAggregateIdentifier String productId,
    @Positive int quantity,
    @NotBlank String orderId,
    @NotBlank String userId,
    @NotBlank String reason
) {}
