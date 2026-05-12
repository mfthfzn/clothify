package com.github.mfthfzn.controller;

import com.github.mfthfzn.dto.LoginRequest;
import com.github.mfthfzn.dto.LoginResponse;
import com.github.mfthfzn.dto.UserResponse;
import com.github.mfthfzn.exception.AuthenticateException;
import com.github.mfthfzn.repository.RefreshTokenRepositoryImpl;
import com.github.mfthfzn.repository.UserRepository;
import com.github.mfthfzn.repository.UserRepositoryImpl;
import com.github.mfthfzn.service.AuthService;
import com.github.mfthfzn.service.AuthServiceImpl;
import com.github.mfthfzn.service.TokenService;
import com.github.mfthfzn.service.TokenServiceImpl;
import com.github.mfthfzn.util.JpaUtil;
import com.github.mfthfzn.util.ValidatorUtil;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@WebServlet(urlPatterns = "/api/auth/login")
public class LoginController extends Controller {

  private final UserRepository userRepository =
          new UserRepositoryImpl(JpaUtil.getEntityManagerFactory());

  private final TokenService tokenService =
          new TokenServiceImpl(
                  new RefreshTokenRepositoryImpl(JpaUtil.getEntityManagerFactory())
          );

  private final AuthService authService =
          new AuthServiceImpl(
                  userRepository
          );

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    try {

      LoginRequest loginRequest = new LoginRequest(req.getParameter("email"), req.getParameter("password"));
      Set<ConstraintViolation<Object>> constraintViolations = ValidatorUtil.validate(loginRequest);

      if (!constraintViolations.isEmpty()) {
        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
          sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Data request invalid", Map.of(
                  "message", constraintViolation.getMessage()
          ));
          break;
        }
        return;
      }

      LoginResponse loginResponse = authService.authenticate(loginRequest);

      if (loginResponse.getUser().getRefreshToken() != null) {
        tokenService.removeRefreshToken(loginResponse.getUser().getEmail());
      }

      loginResponse.setAccessToken(tokenService.generateAccessToken(loginResponse));
      loginResponse.setRefreshToken(tokenService.generateRefreshToken(loginResponse));
      tokenService.saveRefreshToken(loginResponse);

      addCookie(resp, "access_token", loginResponse.getAccessToken(), 60 * 60);
      addCookie(resp, "refresh_token", loginResponse.getRefreshToken(), 60 * 60 * 24 * 7);

      UserResponse userResponse = new UserResponse();
      userResponse.setRole(loginResponse.getUser().getRole().toString());
      sendSuccess(resp, HttpServletResponse.SC_OK, "Login success", userResponse);

    } catch (AuthenticateException authenticateException) {
      sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Login failed", Map.of(
              "message", authenticateException.getMessage()
      ));
    } catch (PersistenceException persistenceException) {
      sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Login failed", Map.of(
              "message", "An error occurred on the database server."
      ));
    }
  }

}
