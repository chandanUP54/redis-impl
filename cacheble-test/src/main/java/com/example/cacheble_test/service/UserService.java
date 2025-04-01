package com.example.cacheble_test.service;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.cacheble_test.modal.User;
import com.example.cacheble_test.repo.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(value = "users", key = "#id")
    public User getUserById(int id) {
        System.out.println("Fetching from database...");
        System.out.println("--fetch from db----");
        System.out.println("--database will work---");
        return userRepository.findById(id);
    }

    @CachePut(value = "users", key = "#user.id")
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}

