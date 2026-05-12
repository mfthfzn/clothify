package com.github.mfthfzn.repository;

import com.github.mfthfzn.entity.User;

import java.util.Optional;

public interface UserRepository {

  Optional<User> findByEmail(String email);

}
