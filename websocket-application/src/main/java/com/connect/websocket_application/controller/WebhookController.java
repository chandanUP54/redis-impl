package com.connect.websocket_application.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.connect.websocket_application.modal.WebhookEvent;
import com.connect.websocket_application.service.WebhookService;


@RestController
@RequestMapping("/api/webhook")
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

 
 @PostMapping("/viewed")
 public String  viewedNotification() {
	 
	 System.out.println("from message viewed: ");
    
     return "sent";
 }

}
