package com.onlinestore.payment.mapper;

import com.onlinestore.payment.core.data.PaymentEntity;
import com.onlinestore.shared.command.ProcessPaymentCommand;
import com.onlinestore.shared.event.PaymentProcessedEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentProcessedEvent toPaymentProcessedEvent(ProcessPaymentCommand command);

    PaymentEntity toPaymentEntity(PaymentProcessedEvent event);

}
