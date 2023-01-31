package com.onlinestore.product.command.rest;

import com.onlinestore.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class ProductCommandController {

    CommandGateway commandGateway;
    ProductMapper mapper;

    @PostMapping
    public String createProduct(@Valid @RequestBody CreateProductRequest request) {
        var createProductCommand = mapper.toCreateProductCommand(request, UUID.randomUUID().toString());
        return commandGateway.sendAndWait(createProductCommand);
    }

}
