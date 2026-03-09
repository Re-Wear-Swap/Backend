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

    // Si ya existe → devolverlo en lugar de lanzar error
    return userRepository.findByEmail(user.getEmail())
        .orElseGet(() -> userRepository.save(user));
  }

  @Override
  public User loginUser(String name, String email) {

    // Buscar por email (único)
    return userRepository.findByEmail(email)
        .orElseGet(() -> {
          // Si no existe → crearlo automáticamente
          User newUser = new User();
          newUser.setName(name);
          newUser.setEmail(email);
          newUser.setIsAdult(true); // 🔥 obligatorio
          newUser.setPoints(3);
          return userRepository.save(newUser);
        });
  }

  @Override
  public User getUserById(Integer id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
  }
}
