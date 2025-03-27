package com.connect.websocket_application.controller;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;


@Controller
public class NotificationController {
	
    @MessageMapping("/notify")  //sender -> /app/notify  
    @SendTo("/topic/notifications")   // receiver -> /topic/notifications
    public String sendNotification(String message) {
    	System.out.println("webhook connect");
        return message; // This message is sent to subscribers
    }
    
}


