package com.impl.webhook.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.impl.webhook.modal.User;
import com.impl.webhook.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
//@CrossOrigin("*","https://webhook-app-test.vercel.app")
//@CrossOrigin(origins = {"http://localhost:3000", "https://webhook-app-test.vercel.app"})
public class UserController {
 
 private final UserService userService;

 public UserController(UserService userService) {
     this.userService = userService;
 }

 @PostMapping
 public ResponseEntity<User> createUser(@RequestBody User user) {
     User createdUser = userService.addUser(user);
     return ResponseEntity.ok(createdUser);
 }
}
