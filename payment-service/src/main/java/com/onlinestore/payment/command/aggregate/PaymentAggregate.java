package com.onlinestore.payment.command.aggregate;

import com.onlinestore.payment.mapper.PaymentMapper;
import com.onlinestore.shared.command.ProcessPaymentCommand;
import com.onlinestore.shared.event.PaymentProcessedEvent;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Data
@NoArgsConstructor
public class PaymentAggregate {

    @AggregateIdentifier
    String paymentId;
    String orderId;

    @CommandHandler
    public PaymentAggregate(ProcessPaymentCommand command, PaymentMapper mapper) {
        AggregateLifecycle.apply(mapper.toPaymentProcessedEvent(command));
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent event) {
        this.paymentId = event.paymentId();
        this.orderId = event.orderId();
    }

}
