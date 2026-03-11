package com.rewear.rewear.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.rewear.rewear.entity.User;
import com.rewear.rewear.service.UserService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public User createUser(@Valid @RequestBody User user) {
    return userService.createUser(user);
  }

  @PostMapping("/login")
  public User login(@RequestParam String name, @RequestParam String email) {
    return userService.loginUser(name, email);
  }

  @GetMapping("/{id}")
  public User getUserById(@PathVariable Integer id) {
    return userService.getUserById(id);
  }
}
