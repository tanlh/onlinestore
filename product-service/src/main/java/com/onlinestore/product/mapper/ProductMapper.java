package com.onlinestore.product.mapper;

import com.onlinestore.product.command.commands.CreateProductCommand;
import com.onlinestore.product.command.rest.CreateProductRequest;
import com.onlinestore.product.core.data.ProductEntity;
import com.onlinestore.product.core.events.ProductCreatedEvent;
import com.onlinestore.product.query.rest.ProductResponse;
import com.onlinestore.shared.command.CancelProductReservationCommand;
import com.onlinestore.shared.event.ProductReservationCancelledEvent;
import com.onlinestore.shared.event.ProductReservedEvent;
import com.onlinestore.shared.command.ReserveProductCommand;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    CreateProductCommand toCreateProductCommand(CreateProductRequest request, String productId);

    ProductCreatedEvent toProductCreatedEvent(CreateProductCommand command);

    ProductEntity toProductEntity(ProductCreatedEvent event);

    ProductResponse toProductResponse(ProductEntity entity);
    List<ProductResponse> toProductResponses(List<ProductEntity> entities);

    ProductReservedEvent toProduceReservedEvent(ReserveProductCommand command);

    ProductReservationCancelledEvent toProductReservationCancelledEvent(CancelProductReservationCommand command);

}
