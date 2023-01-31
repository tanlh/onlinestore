package com.onlinestore.product.query.rest;

import com.onlinestore.product.query.queries.FindAllProductsQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductQueryController {

    final QueryGateway queryGateway;

    @GetMapping
    public List<ProductResponse> getProducts() {
        return queryGateway.query(new FindAllProductsQuery(), ResponseTypes.multipleInstancesOf(ProductResponse.class)).join();
    }

}
