package com.onlinestore.product.query.handler;

import com.onlinestore.product.core.data.ProductRepository;
import com.onlinestore.product.mapper.ProductMapper;
import com.onlinestore.product.query.queries.FindAllProductsQuery;
import com.onlinestore.product.query.rest.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class ProductQueryHandler {

    ProductRepository productRepository;
    ProductMapper mapper;

    @QueryHandler
    public List<ProductResponse> handle(FindAllProductsQuery query) {
        return mapper.toProductResponses(productRepository.findAll());
    }

}
