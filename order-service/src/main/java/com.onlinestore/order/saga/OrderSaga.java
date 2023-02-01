package com.onlinestore.order.saga;

import com.onlinestore.order.command.commands.ApproveOrderCommand;
import com.onlinestore.order.core.events.OrderApprovedEvent;
import com.onlinestore.order.core.events.OrderCreatedEvent;
import com.onlinestore.order.core.events.OrderRejectedEvent;
import com.onlinestore.order.mapper.OrderMapper;
import com.onlinestore.shared.command.ProcessPaymentCommand;
import com.onlinestore.shared.event.PaymentProcessedEvent;
import com.onlinestore.shared.event.ProductReservationCancelledEvent;
import com.onlinestore.shared.event.ProductReservedEvent;
import com.onlinestore.shared.model.User;
import com.onlinestore.shared.query.FetchUserPaymentDetailsQuery;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Saga
@Slf4j
@Setter(onMethod_ = {@Autowired})
public class OrderSaga {

    transient OrderMapper mapper;
    transient CommandGateway commandGateway;
    transient QueryGateway queryGateway;
    transient DeadlineManager deadlineManager;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent event) {
        var reserveProductCommand = mapper.toReserveProductCommand(event);

        commandGateway.send(reserveProductCommand, (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                // start compensation transaction
            } else {
                log.info("Send ReserveProductCommand success: {}", commandResultMessage.getPayload());
            }
        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent event) {
        var fetchUserPaymentDetailsQuery = new FetchUserPaymentDetailsQuery(event.userId());
        User userPaymentDetails;
        try {
            userPaymentDetails = queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
        } catch (Exception ex) {
            this.cancelProductReservation(event, "Send FetchUserPaymentDetailsQuery got error: " + ex.getMessage());
            return;
        }

        if (userPaymentDetails == null) {
            this.cancelProductReservation(event, "FetchUserPaymentDetailsQuery resulted in NULL");
            return;
        }

        log.info("Successfully fetch user payment details: {}", userPaymentDetails);

        deadlineManager.schedule(Duration.ofSeconds(10), "payment-process-deadline", event);

        // testing deadline
//        if (true) return;

        var processPaymentCommand = ProcessPaymentCommand.builder()
            .paymentId(UUID.randomUUID().toString())
            .orderId(event.orderId())
            .paymentDetails(userPaymentDetails.paymentDetails())
            .build();

        String result;
        try {
            result = commandGateway.sendAndWait(processPaymentCommand, 10, TimeUnit.SECONDS);
        } catch (Exception ex) {
            this.cancelProductReservation(event, "Send ProcessPaymentCommand got error: " + ex.getMessage());
            return;
        }

        if (result == null) {
            this.cancelProductReservation(event, "ProcessPaymentCommand resulted in NULL");
            return;
        }

        log.info("Successfully handle process payment command: {}", processPaymentCommand);
    }

    private void cancelProductReservation(ProductReservedEvent event, String reason) {
        log.error(reason);
        deadlineManager.cancelAll("payment-process-deadline");
        commandGateway.send(mapper.toCancelProductReservationCommand(event, reason));
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent event) {
        deadlineManager.cancelAll("payment-process-deadline");
        commandGateway.send(new ApproveOrderCommand(event.orderId()));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApprovedEvent event) {
        log.info("Order is approved, order saga is complete for orderId: " + event.getOrderId());
        // alternative way
//        SagaLifecycle.end();
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservationCancelledEvent event) {
        commandGateway.send(mapper.toRejectOrderCommand(event));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderRejectedEvent event) {
        log.info("Successfully rejected order with orderId: " + event.getOrderId());
    }

    @DeadlineHandler(deadlineName = "payment-process-deadline")
    public void handlePaymentProcessDeadline(ProductReservedEvent event) {
        this.cancelProductReservation(event, "Payment process time out, deadline triggers compensating transaction");
    }

}
