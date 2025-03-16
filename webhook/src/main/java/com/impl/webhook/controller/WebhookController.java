package com.impl.webhook.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.impl.webhook.modal.WebhookEvent;
import com.impl.webhook.services.WebhookService;

@RestController
@RequestMapping("/api/webhook")
@CrossOrigin(origins = {"http://localhost:3000", "https://webhook-app-test.vercel.app"})

public class WebhookController {

 private final WebhookService webhookService;

 public WebhookController(WebhookService webhookService) {
     this.webhookService = webhookService;
 }

 @PostMapping("/receive")
 public ResponseEntity<WebhookEvent> receiveWebhook(
         @RequestHeader("X-Webhook-Source") String source,
         @RequestBody String payload,
         @RequestParam("eventType") String eventType) {
     
     WebhookEvent processedEvent = webhookService.processWebhook(eventType, payload, source);
     return ResponseEntity.ok(processedEvent);
 }


}