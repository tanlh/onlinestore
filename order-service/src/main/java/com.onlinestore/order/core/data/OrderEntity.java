package com.onlinestore.order.core.data;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "orders")
@Data
public class OrderEntity {

    @Id
    String orderId;

    String productId;

    String userId;

    int quantity;

    String addressId;

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

}
