package com.connect.websocket_application.controller;



import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.connect.websocket_application.modal.User;
import com.connect.websocket_application.service.UserService;


@RestController
@RequestMapping("/api/users")
public class UserController {
 
 private final UserService userService;

 public UserController(UserService userService) {
     this.userService = userService;
 }

 @PostMapping("/register")
 public ResponseEntity<User> createUser(@RequestBody User user) {
     User createdUser = userService.addUser(user);
     return ResponseEntity.ok(createdUser);
 }
 
 @GetMapping("/all")
 public List<User> allUser(){
	 return userService.findAllUser();
 }
 
}
