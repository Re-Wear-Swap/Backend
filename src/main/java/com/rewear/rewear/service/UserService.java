package com.rewear.rewear.service;

import com.rewear.rewear.entity.User;

public interface UserService {

  public User createUser(User user);

  public User loginUser(String name, String email);

  public User getUserById(Integer Id);

}
