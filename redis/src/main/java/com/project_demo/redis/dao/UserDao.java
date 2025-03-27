package com.project_demo.redis.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.project_demo.redis.modal.User;

import io.lettuce.core.RedisException;

import java.net.ConnectException;
import java.util.Map;

@Repository
public class UserDao {

	@Autowired(required = false)
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private ApplicationContext applicationContext;

	private static final String KEY = "USER312412";

	public User save(User user) {
		redisTemplate.opsForHash().put(KEY, user.getUserId(), user);
		return user;
	}

	public User get(String userId)  {

		try {
			if (redisTemplate!=null) {
				return (User) redisTemplate.opsForHash().get(KEY, userId);

			}else {
				return new User("R N A", "R N A", "R N A", "R N A");
			}
		} catch (Exception e) {
			refreshRedisTemplate();
			return new User("no  redis", "no redis", "no redis", "restart common service");
		}
	}
	

    private synchronized void refreshRedisTemplate() {
        try {
            this.redisTemplate = applicationContext.getBean(RedisTemplate.class);
        } catch (Exception e) {
            System.err.println("Failed to refresh RedisTemplate: " + e.getMessage());
        }
    }

	// find all
	public Map<Object, Object> findAll() {
		return redisTemplate.opsForHash().entries(KEY);
	}

	// delete
	public void delete(String userId) {
		redisTemplate.opsForHash().delete(KEY, userId);
	}

}