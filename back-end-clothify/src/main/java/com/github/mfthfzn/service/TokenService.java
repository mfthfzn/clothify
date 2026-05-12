package com.github.mfthfzn.service;

import com.github.mfthfzn.dto.LoginResponse;
import com.github.mfthfzn.dto.UserResponse;

public interface TokenService {

  String generateAccessToken(LoginResponse loginResponse);

  String generateAccessToken(UserResponse userResponse);

  String generateRefreshToken(LoginResponse loginResponse);

  void saveRefreshToken(LoginResponse loginResponse);

  void verifyRefreshToken(String token);

  void verifyAccessToken(String token);

  UserResponse getUserFromToken(String token);

  String getRefreshToken(String token);

  void removeRefreshToken(String email);

}
