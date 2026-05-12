package com.github.mfthfzn.service;

import com.github.mfthfzn.dto.LoginRequest;
import com.github.mfthfzn.dto.LoginResponse;
import com.github.mfthfzn.entity.User;
import com.github.mfthfzn.exception.AuthenticateException;
import com.github.mfthfzn.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;

  public AuthServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public LoginResponse authenticate(LoginRequest loginRequest) {
    User user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new AuthenticateException("Email or Password incorrect"));

    if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
      System.out.println(loginRequest.getPassword() + " "+ user.getPassword());
      throw new AuthenticateException("Email or Password incorrect");
    }
    return new LoginResponse(null, null, user);
  }
}
