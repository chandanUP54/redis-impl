package com.project_demo.redis.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.project_demo.redis.config.RedisTemplateWrapper;
import com.project_demo.redis.modal.User;

import io.lettuce.core.RedisException;

import java.net.ConnectException;
import java.util.Arrays;
import java.util.Map;

@Repository
public class UserDao {

	@Autowired
	private RedisTemplateWrapper redisTemplateWrapper;
	
	@Autowired
	private ApplicationContext applicationContext;

	private static final String KEY = "USER312412";

	public User save(User user) {
	    RedisTemplate<String, Object> redisTemplate = redisTemplateWrapper.getRedisTemplate();

		redisTemplate.opsForHash().put(KEY, user.getUserId(), user);
		return user;
	}

	public User get(String userId)  {
	    RedisTemplate<String, Object> redisTemplate = redisTemplateWrapper.getRedisTemplate();


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
	

//    private synchronized void refreshRedisTemplate() {
//        try {
//            this.redisTemplate = applicationContext.getBean(RedisTemplate.class);
//        } catch (Exception e) {
//            System.err.println("Failed to refresh RedisTemplate: " + e.getMessage());
//        }
//    }

	 private void refreshRedisTemplate() {
         

	 }
	
	// find all
	public Map<Object, Object> findAll() {
	    RedisTemplate<String, Object> redisTemplate = redisTemplateWrapper.getRedisTemplate();

		return redisTemplate.opsForHash().entries(KEY);
	}

	// delete
	public void delete(String userId) {
	    RedisTemplate<String, Object> redisTemplate = redisTemplateWrapper.getRedisTemplate();

		redisTemplate.opsForHash().delete(KEY, userId);
	}

}