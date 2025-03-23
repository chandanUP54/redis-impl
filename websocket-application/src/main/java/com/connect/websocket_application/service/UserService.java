package com.connect.websocket_application.service;



import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.connect.websocket_application.modal.User;
import com.connect.websocket_application.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

@Service
public class UserService {
 
 private static final Logger logger = LoggerFactory.getLogger(UserService.class);
 private final UserRepository userRepository;
 private final RestTemplate restTemplate;
 private final ObjectMapper objectMapper;
 private final SimpMessagingTemplate messagingTemplate;
 private final String webhookUrl = "http://localhost:8080/api/webhook/receive";
// private final String webhookUrl = "https://webhook-demo-ykes.onrender.com/api/webhook/receive";

 public UserService(UserRepository userRepository, RestTemplate restTemplate, 
                   SimpMessagingTemplate messagingTemplate) {
     this.userRepository = userRepository;
     this.restTemplate = restTemplate;
     this.messagingTemplate = messagingTemplate;
     this.objectMapper = new ObjectMapper();
 }

 public User addUser(User user) {
     try {
         User savedUser = userRepository.save(user);
         logger.info("New user added with ID: {}", savedUser.getId());
         
         String payload = objectMapper.writeValueAsString(savedUser);
         sendWebhookNotification("USER_CREATED", payload);
         
         // Send WebSocket notification to admins
         messagingTemplate.convertAndSend("/topic/admin-notifications", 
             "New user created: " + savedUser.getName());
         
         return savedUser;
     } catch (Exception e) {
         logger.error("Error adding user: {}", e.getMessage());
         throw new RuntimeException("Failed to add user", e);
     }
 }

 private void sendWebhookNotification(String eventType, String payload) {
     try {
         HttpHeaders headers = new HttpHeaders();
         headers.setContentType(MediaType.APPLICATION_JSON);
         headers.set("X-Webhook-Source", "user-service");
         
         HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);
         String url = webhookUrl + "?eventType=" + eventType;
         
         restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
         logger.info("Webhook notification sent successfully for event: {}", eventType);
     } catch (Exception e) {
         logger.error("Failed to send webhook notification: {}", e.getMessage());
     }
 }
}
