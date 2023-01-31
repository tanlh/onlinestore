package com.onlinestore.product.command.aggregate;

import com.onlinestore.product.command.commands.CreateProductCommand;
import com.onlinestore.product.core.events.ProductCreatedEvent;
import com.onlinestore.product.mapper.ProductMapper;
import com.onlinestore.shared.command.CancelProductReservationCommand;
import com.onlinestore.shared.command.ReserveProductCommand;
import com.onlinestore.shared.event.ProductReservationCancelledEvent;
import com.onlinestore.shared.event.ProductReservedEvent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import javax.validation.ValidationException;
import java.math.BigDecimal;

@Aggregate
@Data
@NoArgsConstructor
@Slf4j
public class ProductAggregate {

    @AggregateIdentifier
    String productId;
    String title;
    BigDecimal price;
    int quantity;

    @CommandHandler
    public ProductAggregate(CreateProductCommand command, ProductMapper mapper) {
        AggregateLifecycle.apply(mapper.toProductCreatedEvent(command));

//        if (new Random().nextBoolean()) {
//            // This error will be wrapped by CommandExecutionException
//            throw new RuntimeException("Order aggregate throw error");
//        }
    }

    @CommandHandler
    public void handle(ReserveProductCommand command, ProductMapper mapper) {
        log.info("Handle ReserveProductCommand: {}", command);
        if (quantity < command.quantity()) {
            throw new ValidationException("Product %s is out of stock".formatted(productId));
        }

        AggregateLifecycle.apply(mapper.toProduceReservedEvent(command));
    }

    @CommandHandler
    public void handle(CancelProductReservationCommand command, ProductMapper mapper) {
        AggregateLifecycle.apply(mapper.toProductReservationCancelledEvent(command));
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent event) {
        BeanUtils.copyProperties(event, this);
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent event) {
        this.quantity -= event.quantity();
    }

    @EventSourcingHandler
    public void on(ProductReservationCancelledEvent event) {
        this.quantity += event.quantity();
    }

}
