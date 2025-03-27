package com.project_demo.redis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Component
public class RedisTemplateWrapper {
	
    private static final Logger logger = LoggerFactory.getLogger(RedisTemplateWrapper.class);

    @Value("${spring.redis.host:localhost}")
	private String redisHost;

	@Value("${spring.redis.port:6379}")
	private int redisPort;

	@Value("${spring.redis.password:}")
	private String redisPassword;


	@Autowired(required = false)
	private RedisTemplate<String, Object> redisTemplate;
	
	 @Autowired
	private RedisConnectionFactory redisConnectionFactory;

	private volatile boolean redisDown = false;

//    public RedisTemplate<String, Object> getRedisTemplate() {
//        if (redisDown) {
//            return null; // Return null if Redis is down
//        }
//
//        try {
//            // Test Redis connection
//        	
//        	System.out.println("testing redis connection");
//        	
//            redisTemplate.hasKey("test"); 
//            return redisTemplate;
//        } catch (DataAccessException e) {
//            redisDown = true; // Mark Redis as down
//            return null;
//        }
//    }


//    public RedisTemplate<String, Object> getRedisTemplate() {
//        return redisDown ? null : redisTemplate;
//    }

//    @Scheduled(fixedRate = 10000) // Check every 10 seconds
//    public void checkRedisConnection() {
//        logger.info("Running checkRedisConnection...");
//
//        try {
//            String response = redisConnectionFactory.getConnection().ping(); // Use RedisConnectionFactory
//            redisDown = !"PONG".equals(response);
//            logger.info("Redis is {}", redisDown ? "DOWN" : "UP");
//            System.out.println("response: "+response);
//
//        } catch (Exception e) {
//            logger.error("Error checking Redis connection: {}", e.getMessage());
//
//            redisDown = true;
//        }
//    }
    public RedisTemplate<String, Object> getRedisTemplate() {
        if (redisTemplate == null) {
            logger.warn("RedisTemplate is not available (null)");
            return null;
        }
        return redisDown ? null : redisTemplate;
    }

    @Scheduled(fixedRate = 5000, initialDelay = 1000) // Check every 5s, start after 1s
    public void checkRedisConnection() {
        logger.info("Checking Redis connection status...");
        boolean previousState = redisDown;
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            // Connect to Redis
            socket = new Socket(redisHost, redisPort);
            socket.setSoTimeout(2000); // 2-second timeout
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Authenticate if password is set
            if (redisPassword != null && !redisPassword.isEmpty()) {
                out.println("AUTH " + redisPassword);
                String authResponse = in.readLine();
                if (!"+OK".equals(authResponse)) {
                    throw new RuntimeException("Authentication failed: " + authResponse);
                }
                logger.debug("Authentication successful");
            }

            // Send PING command
            out.println("PING");
            String response = in.readLine();

            if ("+PONG".equals(response)) {
                if (previousState) {
                    logger.info("✅ Redis is back online! Transitioning from DOWN to UP");
                    redisDown = false;
                } else {
                    logger.debug("✅ Redis remains UP");
                }
            } else {
                if (!previousState) {
                    logger.warn("⚠️ Redis responded with unexpected value: '{}'. Marking as DOWN", response);
                    redisDown = true;
                } else {
                    logger.warn("⚠️ Redis still DOWN with unexpected response: '{}'", response);
                }
            }
        } catch (Exception e) {
            if (!previousState) {
                logger.error("❌ Redis connection failed! Transitioning from UP to DOWN. Error: {}", e.getMessage(), e);
                redisDown = true;
            } else {
                logger.debug("❌ Redis remains DOWN. Error: {}", e.getMessage());
            }
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
                logger.debug("Socket resources closed successfully");
            } catch (Exception e) {
                logger.warn("Failed to close socket resources: {}", e.getMessage());
            }
        }

        logger.info("Current redisDown state: {}", redisDown);
    }
    public boolean isRedisDown() {
        return redisDown;
    }
}
