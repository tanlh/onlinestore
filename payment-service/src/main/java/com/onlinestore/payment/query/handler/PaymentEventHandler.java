package com.onlinestore.payment.query.handler;

import com.onlinestore.payment.core.data.PaymentRepository;
import com.onlinestore.payment.mapper.PaymentMapper;
import com.onlinestore.shared.event.PaymentProcessedEvent;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@ProcessingGroup("payment")
@Slf4j
public class PaymentEventHandler {

    PaymentRepository paymentRepository;
    PaymentMapper mapper;

    @EventHandler
    public void on(PaymentProcessedEvent event) {
        paymentRepository.save(mapper.toPaymentEntity(event));
    }

}
