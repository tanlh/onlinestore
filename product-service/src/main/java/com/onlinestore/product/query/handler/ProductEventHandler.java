package com.onlinestore.product.query.handler;

import com.onlinestore.product.core.data.ProductRepository;
import com.onlinestore.product.core.events.ProductCreatedEvent;
import com.onlinestore.product.mapper.ProductMapper;
import com.onlinestore.product.query.queries.FindAllProductsQuery;
import com.onlinestore.product.query.rest.ProductResponse;
import com.onlinestore.shared.event.ProductReservationCancelledEvent;
import com.onlinestore.shared.event.ProductReservedEvent;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

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

//        var random = new Random();
//        if (random.nextBoolean()) {
//            if (random.nextBoolean()) {
//                throw new IllegalStateException("Product event handler illegal error");
//            }
//
//            throw new RuntimeException("Product event handler runtime error");
//        }
    }

    @EventHandler
    public void on(ProductReservedEvent event) {
        productRepository.deductQuantity(event.productId(), event.quantity());
    }

    @EventHandler
    public void on(ProductReservationCancelledEvent event) {
        productRepository.increaseQuantity(event.productId(), event.quantity());
    }

    @QueryHandler
    public List<ProductResponse> handle(FindAllProductsQuery query) {
        return mapper.toProductResponses(productRepository.findAll());
    }

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalArgumentException ex) {
        log.error("Illegal error happens", ex);

        // rethrow to propagate exception to let axon rollback the transaction
        // but it's not enough, we have to register PropagatingExceptionHandler (type of ListenerInvocationErrorHandler) also
        throw ex;
    }

    @ExceptionHandler
    public void handle(Exception ex) throws Exception {
        log.error("Other error happens", ex);
        throw ex;
    }

}
