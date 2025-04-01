package com.example.cacheble_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CachebleTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CachebleTestApplication.class, args);
	}

}
