package com.connect.websocket_application.controller;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;


@Controller
//@CrossOrigin(origins = {"http://localhost:3000", "https://webhook-app-test.vercel.app"})
public class NotificationController {
    @MessageMapping("/notify")
    @SendTo("/topic/notifications")
    public String sendNotification(String message) {
    	System.out.println("webhook connect");
        return message; // This message is sent to subscribers
    }
    
}


