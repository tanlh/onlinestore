package com.onlinestore.product.core.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductLookupRepository extends JpaRepository<ProductLookupEntity, String> {

    Optional<ProductLookupEntity> findByProductIdOrTitle(String productId, String title);

}
