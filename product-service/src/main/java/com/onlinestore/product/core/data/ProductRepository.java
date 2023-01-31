package com.onlinestore.product.core.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE products SET quantity = quantity - :quantity WHERE product_id = :productId",
        nativeQuery = true)
    int deductQuantity(String productId, int quantity);

    @Transactional
    @Modifying
    @Query(value = "UPDATE products SET quantity = quantity + :quantity WHERE product_id = :productId",
        nativeQuery = true)
    int increaseQuantity(String productId, int quantity);

}
