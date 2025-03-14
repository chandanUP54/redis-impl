package com.impl.webhook.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.impl.webhook.modal.User;
import com.impl.webhook.repository.UserRepository;

@Service
public class UserService {
 
 private static final Logger logger = LoggerFactory.getLogger(UserService.class);
 private final UserRepository userRepository;
 private final RestTemplate restTemplate;
 private final ObjectMapper objectMapper;
 private final String webhookUrl = "http://localhost:8080/api/webhook/receive"; // Configure this in application.yaml

 public UserService(UserRepository userRepository, RestTemplate restTemplate) {
     this.userRepository = userRepository;
     this.restTemplate = restTemplate;
     this.objectMapper = new ObjectMapper();
 }

 public User addUser(User user) {
     try {
         // Save the user
         User savedUser = userRepository.save(user);
         logger.info("New user added with ID: {}", savedUser.getId());
         
         // Prepare webhook payload
         String payload = objectMapper.writeValueAsString(savedUser);
         
         // Send webhook notification
         sendWebhookNotification("USER_CREATED", payload);
         
         return savedUser;
     } catch (Exception e) {
         logger.error("Error adding user: {}", e.getMessage());
         throw new RuntimeException("Failed to add user", e);
     }
 }

// private void sendWebhookNotification(String eventType, String payload) {
//     try {
//         String url = webhookUrl + "?eventType=" + eventType;
//         restTemplate.postForObject(
//             url,
//             payload,
//             String.class,
//             "user-service"
//         );
//         logger.info("Webhook notification sent successfully for event: {}", eventType);
//     } catch (Exception e) {
//         logger.error("Failed to send webhook notification: {}", e.getMessage());
//         // Note: In production, you might want to implement a retry mechanism or queue
//     }
// }
 
 private void sendWebhookNotification(String eventType, String payload) {
	    try {
	        // Create HTTP headers
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.set("X-Webhook-Source", "user-service"); // Set the required header
	        
	        // Create the request entity with payload and headers
	        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);
	        
	        // Construct the URL with query parameter
	        String url = webhookUrl + "?eventType=" + eventType;
	        
	        // Send the request
	        ResponseEntity<String> response = restTemplate.exchange(
	            url,
	            HttpMethod.POST,
	            requestEntity,
	            String.class
	        );
	        
	        logger.info("Webhook notification sent successfully for event: {}. Response: {}", 
	            eventType, response.getStatusCode());
	    } catch (Exception e) {
	        logger.error("Failed to send webhook notification: {}", e.getMessage());
	        throw new RuntimeException("Webhook notification failed", e);
	    }
	}
 
}