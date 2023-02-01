package com.onlinestore.order.core.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, String> {

    OrderEntity findByOrderId(String orderId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE orders SET order_status = :orderStatus WHERE order_id = :orderId", nativeQuery = true)
    int updateOrderStatus(String orderId, String orderStatus);

}
