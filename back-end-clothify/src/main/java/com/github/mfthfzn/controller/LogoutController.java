package com.github.mfthfzn.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.mfthfzn.dto.UserResponse;
import com.github.mfthfzn.repository.RefreshTokenRepositoryImpl;
import com.github.mfthfzn.service.TokenService;
import com.github.mfthfzn.service.TokenServiceImpl;
import com.github.mfthfzn.util.JpaUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet(urlPatterns = "/api/auth/logout")
public class LogoutController extends Controller {

  private final TokenService tokenService =
          new TokenServiceImpl(
                  new RefreshTokenRepositoryImpl(JpaUtil.getEntityManagerFactory())
          );

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    try {

      String refreshToken = getCookieValue(req, "refresh_token");
      if (refreshToken != null && !refreshToken.isBlank()) {
        UserResponse userFromToken = tokenService.getUserFromToken(refreshToken);
        tokenService.removeRefreshToken(userFromToken.getEmail());
      }

      removeCookie(resp, "access_token");
      removeCookie(resp, "refresh_token");

      sendSuccess(resp, HttpServletResponse.SC_OK, "Success to logout", Map.of(
              "message", "Success to delete jwt token"
      ));
    } catch (JWTVerificationException exception) {
      removeCookie(resp, "access_token");
      removeCookie(resp, "refresh_token");
      sendSuccess(resp, HttpServletResponse.SC_OK, "Session already cleared", null);

    } catch (RuntimeException exception) {
      sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Logout failed", Map.of(
              "message", "Server database error"
      ));
    }

  }

}
