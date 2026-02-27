package com.rewear.rewear.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rewear.rewear.entity.User;
import com.rewear.rewear.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User createUser(User user) {
    if (userRepository.existsByEmail(user.getEmail())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya esiste un usuario con este email");
    }
    return userRepository.save(user);
  }

  @Override
  public User loginUser(String name, String email) {
    return userRepository.findByNameAndEmail(name, email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
  }

  @Override
  public User getUserById(Integer Id) {
    return userRepository.findById(Id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
  }
}
