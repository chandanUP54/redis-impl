package com.connect.websocket_application.modal;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.LocalDateTime;

@Entity
@Data
public class WebhookEvent {
 
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 
 private String eventType;
 private String payload;
 private String source;
 private LocalDateTime receivedAt;
 private String status;

 // Constructors
 public WebhookEvent() {}
 
 public WebhookEvent(String eventType, String payload, String source) {
     this.eventType = eventType;
     this.payload = payload;
     this.source = source;
     this.receivedAt = LocalDateTime.now();
     this.status = "RECEIVED";
 }

 // Getters and Setters
 // ... (generate getters and setters for all fields)
}
