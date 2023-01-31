package com.onlinestore.payment.core.data;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "payments")
@Data
public class PaymentEntity {

    @Id
    String paymentId;

    String orderId;

}
