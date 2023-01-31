package com.onlinestore.order.command.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record ApproveOrderCommand(
    @TargetAggregateIdentifier String orderId
) {}
