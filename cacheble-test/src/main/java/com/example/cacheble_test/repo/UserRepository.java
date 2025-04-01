package com.example.cacheble_test.repo;


import org.springframework.stereotype.Repository;

import com.example.cacheble_test.modal.User;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {
    private final Map<Integer, User> database = new HashMap<>();

    public User save(User user) {
        database.put(user.getId(), user);
        return user;
    }

    public User findById(int id) {
        return database.get(id);
    }

    public void deleteById(int id) {
        database.remove(id);
    }
}

