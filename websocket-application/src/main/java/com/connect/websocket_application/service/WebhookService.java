package com.connect.websocket_application.service;


import org.springframework.stereotype.Service;

import com.connect.websocket_application.modal.WebhookEvent;
import com.connect.websocket_application.repository.WebhookRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class WebhookService {
 
 private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);
 private final WebhookRepository webhookRepository;

 public WebhookService(WebhookRepository webhookRepository) {
     this.webhookRepository = webhookRepository;
 }

 public WebhookEvent processWebhook(String eventType, String payload, String source) {
     try {
         logger.info("Processing webhook - EventType: {}, Source: {}", eventType, source);
         
         WebhookEvent event = new WebhookEvent(eventType, payload, source);
         WebhookEvent savedEvent = webhookRepository.save(event);
         
         logger.info("Webhook processed successfully with ID: {}", savedEvent.getId());
         return savedEvent;
     } catch (Exception e) {
         logger.error("Error processing webhook: {}", e.getMessage());
         throw new RuntimeException("Failed to process webhook", e);
     }
 }
}
