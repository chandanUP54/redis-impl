package com.impl.circuit_braker.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class ExternalApiService {

    // Simulate an unreliable external API call
    @CircuitBreaker(name = "externalService", fallbackMethod = "fallback")
    public String callExternalApi() {
        // Simulate random failures
        if (Math.random() > 0.5) {
            throw new RuntimeException("External API failed!");
        }
        return "Success from external API";
    }

    // Fallback method when the circuit breaker trips or the call fails
    public String fallback(Throwable t) {
        return "Fallback response: External API is down (" + t.getMessage() + ")";
    }
}