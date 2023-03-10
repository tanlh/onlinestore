package com.onlinestore.product.query.handler;

import com.onlinestore.product.core.data.ProductRepository;
import com.onlinestore.product.core.events.ProductCreatedEvent;
import com.onlinestore.product.mapper.ProductMapper;
import com.onlinestore.shared.event.ProductReservationCancelledEvent;
import com.onlinestore.shared.event.ProductReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.stereotype.Component;

import javax.persistence.PersistenceException;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@ProcessingGroup("product")
@Slf4j
public class ProductEventHandler {

    ProductRepository productRepository;
    ProductMapper mapper;

    @EventHandler
    public void on(ProductCreatedEvent event) {
        productRepository.save(mapper.toProductEntity(event));
    }

    @EventHandler
    public void on(ProductReservedEvent event) {
        productRepository.deductQuantity(event.productId(), event.quantity());
    }

    @EventHandler
    public void on(ProductReservationCancelledEvent event) {
        productRepository.increaseQuantity(event.productId(), event.quantity());
    }

    @ExceptionHandler(resultType = PersistenceException.class)
    public void handle(PersistenceException ex) {
        log.error("Error while performing data change to database", ex);
        throw ex;
    }

    @ExceptionHandler
    public void handle(Exception ex) throws Exception {
        log.error("Other error happens", ex);
        throw ex;
    }

    @ResetHandler
    public void reset() {
        productRepository.deleteAll();
    }

}
