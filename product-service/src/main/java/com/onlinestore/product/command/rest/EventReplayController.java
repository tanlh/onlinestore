package com.onlinestore.product.command.rest;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/management")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class EventReplayController {

    EventProcessingConfiguration eventProcessingConfiguration;

    @PostMapping("processors/{processorName}/replay")
    public ResponseEntity<String> replayEvent(@PathVariable String processorName) {
        var eventProcessor = eventProcessingConfiguration.eventProcessor(processorName, TrackingEventProcessor.class)
            .orElseThrow(() -> new NoSuchElementException("Processor %s is not tracking event processor and cannot replay".formatted(processorName)));

        eventProcessor.shutDown();
        eventProcessor.resetTokens();
        eventProcessor.start();

        return ResponseEntity.ok("Successfully replay events");
    }

}
