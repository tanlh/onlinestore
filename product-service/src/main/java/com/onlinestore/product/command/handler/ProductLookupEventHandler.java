package com.onlinestore.product.command.handler;

import com.onlinestore.product.core.data.ProductLookupEntity;
import com.onlinestore.product.core.data.ProductLookupRepository;
import com.onlinestore.product.core.events.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("product")
public class ProductLookupEventHandler {

    final ProductLookupRepository productLookupRepository;

    @EventHandler
    public void on(ProductCreatedEvent event) {
        productLookupRepository.save(
            ProductLookupEntity.builder()
                .productId(event.getProductId())
                .title(event.getTitle())
                .build());
    }

}
