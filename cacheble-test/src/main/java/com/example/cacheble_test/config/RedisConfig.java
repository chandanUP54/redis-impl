package com.example.cacheble_test.config;


import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;

@Configuration
public class RedisConfig {

	@Value("${spring.redis.host:localhost}")
	private String redisHost;

	@Value("${spring.redis.port:6379}")
	private int redisPort;

	@Value("${spring.redis.password:}")
	private String redisPassword;

	
	@Bean(destroyMethod = "shutdown")
	public ClientResources clientResources() {
		return DefaultClientResources.create();
	}

	  @Bean
	    public RedisConnectionFactory redisConnectionFactory() {
	        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
	        redisConfig.setHostName("localhost");
	        redisConfig.setPort(6379);
	        redisConfig.setPassword(redisPassword);

	        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
	                .commandTimeout(Duration.ofSeconds(1))
	                .build();

	        return new LettuceConnectionFactory(redisConfig, clientConfig);
	    }

	@Bean
    @Conditional(RedisAvailableCondition.class)
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
		template.afterPropertiesSet();
		return template;
	}
}
