package com.onlinestore.shared.config;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.config.ConfigurationScopeAwareProvider;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.SimpleDeadlineManager;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.axonframework.messaging.interceptors.BeanValidationInterceptor;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class CommonAxonConfig {

    @Bean
    public MessageDispatchInterceptor<CommandMessage<?>> beanValidationInterceptor() {
        return new BeanValidationInterceptor<>();
    }

    @Bean
    public MessageDispatchInterceptor<CommandMessage<?>> loggingDispatchInterceptor() {
        return messages -> (index, command) -> {
            log.info("Dispatched command: {}", command.getPayload());
            return command;
        };
    }

    @Bean
    public CommandGateway commandGateway(
        CommandBus commandBus,
        List<MessageDispatchInterceptor<? super CommandMessage<?>>> dispatchInterceptors
    ) {
        return DefaultCommandGateway.builder()
            .commandBus(commandBus)
            .dispatchInterceptors(dispatchInterceptors)
            .build();
    }

    @Autowired
    public void configure(EventProcessingConfigurer configurer) {
        configurer.registerDefaultListenerInvocationErrorHandler(conf -> PropagatingErrorHandler.instance());
    }

    @Bean
    public DeadlineManager deadlineManager(org.axonframework.config.Configuration configuration,
                                           SpringTransactionManager transactionManager) {
        return SimpleDeadlineManager.builder()
            .scopeAwareProvider(new ConfigurationScopeAwareProvider(configuration))
            .transactionManager(transactionManager)
            .build();
    }

}
