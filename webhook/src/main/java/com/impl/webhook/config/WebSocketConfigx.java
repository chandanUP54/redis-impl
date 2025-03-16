package com.impl.webhook.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.impl.webhook.services.MyWebSocketHandler;
//
//@Configuration
//@EnableWebSocket
//public class WebSocketConfigx implements WebSocketConfigurer {
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(new MyWebSocketHandler(), "/ws")
//                .setAllowedOrigins("http://localhost:3000");  // Allow all origins for testing
//    }
//}


public class WebSocketConfigx{
	
}
