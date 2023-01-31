package com.onlinestore.shared.command;

import com.onlinestore.shared.model.PaymentDetails;
import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
public record ProcessPaymentCommand(
    @TargetAggregateIdentifier String paymentId,
    @NotBlank String orderId,
    @NotNull PaymentDetails paymentDetails
) {}
