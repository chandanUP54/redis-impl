package com.connect.websocket_application.service;



import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.http.WebSocket;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.connect.websocket_application.dto.WebsocketDTO;
import com.connect.websocket_application.modal.User;
import com.connect.websocket_application.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
 private final String webhookUrl = "http://localhost:9090/api/webhook/receive";
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
    	 
    	 User newUser=new User();
    	 
    	 newUser.setEmail(user.getEmail());
    	 newUser.setName(user.getName());
    	 newUser.setJoiningDate(LocalDate.now());
    	 newUser.setRole(user.getRole());
    	 
    	 
         User savedUser = userRepository.save(newUser);
         logger.info("New user added with ID: {}", savedUser.getId());
         
         
         ObjectMapper objectMapper = new ObjectMapper();
         objectMapper.registerModule(new JavaTimeModule());
         
         String payload = objectMapper.writeValueAsString(savedUser);
         
         WebsocketDTO websocketDTO=new WebsocketDTO();
         
         websocketDTO.setMessage("New User Added Named "+savedUser.getName()+" , Role "+savedUser.getRole());
         websocketDTO.setResponse(payload);
         
         // Send WebSocket notification to admins
         messagingTemplate.convertAndSend("/topic/notifications",websocketDTO);
         
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

public List<User> findAllUser() {
	// TODO Auto-generated method stub
	return userRepository.findAll();
}
}
