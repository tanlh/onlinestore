package com.onlinestore.product.command.interceptor;

import com.onlinestore.product.command.commands.CreateProductCommand;
import com.onlinestore.product.core.data.ProductLookupRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class ProductCreationDispatchInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    final ProductLookupRepository productLookupRepository;

    @NotNull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@NotNull List<? extends CommandMessage<?>> messages) {
        return (index, message) -> {
            if (CreateProductCommand.class.equals(message.getPayloadType())) {
                final var command = (CreateProductCommand) message.getPayload();

                productLookupRepository.findByProductIdOrTitle(command.productId(), command.title())
                    .ifPresent(existProduct -> {
                        throw new IllegalStateException(
                            "Product with productId=%s or title=%s already exists".formatted(command.productId(), command.title()));
                    });
            }

            return message;
        };
    }

}
