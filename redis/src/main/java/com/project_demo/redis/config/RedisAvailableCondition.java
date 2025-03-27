package com.project_demo.redis.config;

import java.time.Duration;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.api.StatefulRedisConnection;

public class RedisAvailableCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String redisHost = context.getEnvironment().getProperty("spring.redis.host", "localhost11");
        int redisPort = context.getEnvironment().getProperty("spring.redis.port", Integer.class, 6379);
        String redisPassword = context.getEnvironment().getProperty("spring.redis.password", "aduhuxvv");
        
//        System.out.println(redisHost+" "+redisPort+" "+redisPassword);

        RedisClient redisClient = null;
        StatefulRedisConnection<String, String> connection = null;

        try {
            // Use RedisURI for the condition check
            RedisURI redisUri = RedisURI.Builder.redis(redisHost, redisPort)
                .withPassword(redisPassword.toCharArray())
                .withTimeout(Duration.ofSeconds(2))  // 2-second timeout
                .build();

            redisClient = RedisClient.create(redisUri);
            redisClient.setOptions(ClientOptions.builder()
                .autoReconnect(false)
                .timeoutOptions(TimeoutOptions.builder()
                    .fixedTimeout(Duration.ofSeconds(2))
                    .build())
                .build());

            connection = redisClient.connect();
            if (!connection.isOpen()) {
                System.out.println("Redis connection failed: Connection not open");
                return false;
            }

            String pingResponse = connection.sync().ping();
            if (pingResponse == null || !pingResponse.equals("PONG")) {
                System.out.println("Redis connection failed: Invalid PING response");
                return false;
            }

            System.out.println("Redis connection successful");
            return true;

        } catch (Exception e) {
            System.out.println("Redis is unavailable: " + e.getMessage());
            return false;
        } finally {
            if (connection != null && connection.isOpen()) {
                connection.close();
            }
            if (redisClient != null) {
                redisClient.shutdown();
            }
        }
    }
}