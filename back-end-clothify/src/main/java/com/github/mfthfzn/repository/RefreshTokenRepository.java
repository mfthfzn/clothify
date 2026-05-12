package com.github.mfthfzn.repository;

import com.github.mfthfzn.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

  void insert(RefreshToken refreshTokenSession);

  Optional<RefreshToken> findByEmail(String email);

  void removeByEmail(String email);

}
