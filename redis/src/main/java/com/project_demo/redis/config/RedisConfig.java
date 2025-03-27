package com.project_demo.redis.config;
//

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Configuration
//public class RedisConfig {
//
//	@Bean
//	public RedisConnectionFactory connectionFactory() {
//
//		return new LettuceConnectionFactory();
//	}
//
//	@Bean
//	public RedisTemplate<String, Object> redisTemplate() {
//
//		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//
//		// connection Factory
//		redisTemplate.setConnectionFactory(connectionFactory());
//
//		// key serializer
//		redisTemplate.setKeySerializer(new StringRedisSerializer());
//
//		// value serializer
//		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//		return redisTemplate;
//
//	}
//
//}

//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import io.lettuce.core.ClientOptions;
//import io.lettuce.core.resource.ClientResources;
//import io.lettuce.core.resource.DefaultClientResources;
//
//@Configuration
//public class RedisConfig {
//
//    @Value("${spring.redis.host:localhost}")
//    private String redisHost;
//
//    @Value("${spring.redis.port:6379}")
//    private int redisPort;
//
//    @Value("${spring.redis.password:}")
//    private String redisPassword;
//
//    @Value("${spring.redis.timeout:2000}")
//    private long timeout;
//
//    @Bean
//    public ClientResources clientResources() {
//        return DefaultClientResources.create();
//    }
//
//
//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory(ClientResources clientResources) {
//        // Basic Redis configuration
//        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
//        redisConfig.setHostName(redisHost);
//        redisConfig.setPort(redisPort);
//        if (!redisPassword.isEmpty()) {
//            redisConfig.setPassword(redisPassword);
//        }
//
//        // Client options with auto-reconnect
//        ClientOptions clientOptions = ClientOptions.builder()
//            .autoReconnect(true)
//            .build();
//
//        // Pooling and client configuration
//        LettucePoolingClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
//            .clientOptions(clientOptions)
//            .clientResources(clientResources)
//            .commandTimeout(java.time.Duration.ofMillis(timeout))
//            .build();
//
//        System.out.println("connection established with redis");
//        
//        return new LettuceConnectionFactory(redisConfig, clientConfig);
//    }
//
//    
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//        template.afterPropertiesSet();
//        return template;
//    }
//}

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
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

	@Value("${spring.redis.timeout:2000}")
	private long timeout;

	@Value("${spring.redis.lettuce.pool.max-active:30}")
	private int maxActive;

	@Value("${spring.redis.lettuce.pool.max-idle:10}")
	private int maxIdle;

	@Value("${spring.redis.lettuce.pool.min-idle:5}")
	private int minIdle;

	@Value("${spring.redis.lettuce.pool.max-wait:-1}")
	private long maxWait;

	@Bean(destroyMethod = "shutdown")
	public ClientResources clientResources() {
		return DefaultClientResources.create();
	}

	@Bean
    @Conditional(RedisAvailableCondition.class)
	public LettuceConnectionFactory redisConnectionFactory(ClientResources clientResources) {
		RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
		redisConfig.setHostName(redisHost);
		redisConfig.setPort(redisPort);
		if (!redisPassword.isEmpty()) {
			redisConfig.setPassword(redisPassword);
		}

		// Disable auto-reconnect
		ClientOptions clientOptions = ClientOptions.builder().autoReconnect(false) // Changed to false
				.build();

		GenericObjectPoolConfig<Object> poolConfig = new GenericObjectPoolConfig<>();
		poolConfig.setMaxTotal(maxActive);
		poolConfig.setMaxIdle(maxIdle);
		poolConfig.setMinIdle(minIdle);
		poolConfig.setMaxWaitMillis(maxWait);

		LettucePoolingClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
				.clientOptions(clientOptions).clientResources(clientResources)
				.commandTimeout(java.time.Duration.ofMillis(timeout)).poolConfig(poolConfig).build();
		
		System.out.println("redis connected");

		return new LettuceConnectionFactory(redisConfig, clientConfig);
	}

	@Bean
	@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
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