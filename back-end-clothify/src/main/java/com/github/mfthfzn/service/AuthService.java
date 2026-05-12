package com.github.mfthfzn.service;

import com.github.mfthfzn.dto.LoginRequest;
import com.github.mfthfzn.dto.LoginResponse;

public interface AuthService {

  LoginResponse authenticate(LoginRequest loginRequest);

}
