package com.example.cacheble_test.config;


import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

public class CustomCacheErrorHandler implements CacheErrorHandler {

    public void handleCacheGetError(RuntimeException exception, Cache cache) {
        System.err.println("Cache get error for " + cache.getName() + ": " + exception.getMessage());
        // Do nothing, let the method proceed
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        System.err.println("Cache put error for " + cache.getName() + ", key " + key + ": " + exception.getMessage());
        // Ignore, proceed without caching
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        System.err.println("Cache evict error for " + cache.getName() + ", key " + key + ": " + exception.getMessage());
        // Ignore
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        System.err.println("Cache clear error for " + cache.getName() + ": " + exception.getMessage());
        // Ignore
    }

	@Override
	public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
		// TODO Auto-generated method stub
		
	}
}