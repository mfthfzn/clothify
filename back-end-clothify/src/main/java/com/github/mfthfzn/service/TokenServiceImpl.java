package com.github.mfthfzn.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.mfthfzn.dto.LoginResponse;
import com.github.mfthfzn.dto.UserResponse;
import com.github.mfthfzn.entity.RefreshToken;
import com.github.mfthfzn.entity.User;
import com.github.mfthfzn.exception.AccessTokenExpiredException;
import com.github.mfthfzn.exception.RefreshTokenExpiredException;
import com.github.mfthfzn.repository.RefreshTokenRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class TokenServiceImpl implements TokenService {

  private final RefreshTokenRepository tokenRepository;

  Algorithm algorithm = Algorithm.HMAC256("OBOY GANTENG SEKALI");

  private static final String CLAIM_ROLE = "role";
  private static final String CLAIM_NAME = "name";
  private static final String CLAIM_STORE_NAME = "store_name";

  private static final long ACCESS_TOKEN_EXPIRY_MINUTES = 5;
  private static final long REFRESH_TOKEN_EXPIRY_DAYS = 7;

  public TokenServiceImpl(RefreshTokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }

  public String createJwt(User user, Duration duration) {
    return JWT.create()
            .withSubject(user.getEmail())
            .withClaim(CLAIM_ROLE, user.getRole().toString())
            .withClaim(CLAIM_NAME, user.getName())
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plus(duration))
            .sign(algorithm);
  }

  @Override
  public String generateAccessToken(LoginResponse loginResponse) {
    User user = loginResponse.getUser();
    return createJwt(user, Duration.ofMinutes(ACCESS_TOKEN_EXPIRY_MINUTES));
  }

  @Override
  public String generateAccessToken(UserResponse userResponse) {
    return JWT.create()
            .withSubject(userResponse.getEmail())
            .withClaim(CLAIM_ROLE, userResponse.getRole())
            .withClaim(CLAIM_NAME, userResponse.getName())
            .withClaim(CLAIM_STORE_NAME, userResponse.getStoreName())
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plus(Duration.ofMinutes(ACCESS_TOKEN_EXPIRY_MINUTES)))
            .sign(algorithm);
  }

  @Override
  public String generateRefreshToken(LoginResponse loginResponse) {
    User user = loginResponse.getUser();
    return createJwt(user, Duration.ofMinutes(REFRESH_TOKEN_EXPIRY_DAYS));
  }

  @Override
  public void saveRefreshToken(LoginResponse loginResponse) {
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUser(loginResponse.getUser());
    refreshToken.setToken(loginResponse.getRefreshToken());
    tokenRepository.insert(refreshToken);
  }

  @Override
  public void verifyRefreshToken(String token) {
    try {
      JWT.require(algorithm)
              .build()
              .verify(token);
    } catch (TokenExpiredException tokenExpiredException) {
      // Throw RefreshTokenExpiredException
      throw new RefreshTokenExpiredException("Refresh token expired");
    } catch (JWTVerificationException jwtVerificationException) {
      throw  new JWTVerificationException("Refresh token invalid");
    }
  }

  @Override
  public void verifyAccessToken(String token) {
    try {
      JWT.require(algorithm)
              .build()
              .verify(token);
    } catch (TokenExpiredException tokenExpiredException) {
      // Throw AccessTokenExpiredException
      throw new AccessTokenExpiredException("Access token expired");
    } catch (JWTVerificationException jwtVerificationException) {
      throw  new JWTVerificationException("Access Token invalid");
    }
  }

  @Override
  public UserResponse getUserFromToken(String token) {

    DecodedJWT decodedJWT = JWT.decode(token);

    UserResponse userResponse = new UserResponse();

    userResponse.setEmail(decodedJWT.getSubject());
    userResponse.setStoreName(decodedJWT.getClaim("store_name").asString());
    userResponse.setRole(decodedJWT.getClaim("role").asString());
    userResponse.setName(decodedJWT.getClaim("name").asString());
    return userResponse;
  }

  @Override
  public String getRefreshToken(String email) {
    try {
      Optional<RefreshToken> refreshToken = tokenRepository.findByEmail(email);
      return refreshToken.map(RefreshToken::getToken).orElse(null);
    } catch (RuntimeException runtimeException) {
      throw new RuntimeException(runtimeException);
    }
  }

  @Override
  public void removeRefreshToken(String email) {
    try {
      tokenRepository.removeByEmail(email);
    } catch (RuntimeException runtimeException) {
      throw new RuntimeException(runtimeException);
    }
  }

}
