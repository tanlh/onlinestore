package com.onlinestore.user.query.handler;

import com.onlinestore.shared.model.PaymentDetails;
import com.onlinestore.shared.model.User;
import com.onlinestore.shared.query.FetchUserPaymentDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserEventHandler {

    @QueryHandler
    public User handle(FetchUserPaymentDetailsQuery query) {
        var paymentDetails = PaymentDetails.builder()
            .cardNumber("Card123")
            .cvv("123")
            .name("Tan Test")
            .validUntilMonth(12)
            .validUntilYear(2030)
            .build();

        return User.builder()
            .firstName("Tan")
            .lastName("Test")
            .userId(query.getUserId())
            .paymentDetails(paymentDetails)
            .build();
    }

}
