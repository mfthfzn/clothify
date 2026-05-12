package com.github.mfthfzn.repository;

import com.github.mfthfzn.entity.User;
import jakarta.persistence.*;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

  private final EntityManagerFactory entityManagerFactory;

  public UserRepositoryImpl(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public Optional<User> findByEmail(String email) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();
      User user = entityManager.find(User.class, email);
      transaction.commit();
      return Optional.ofNullable(user);
    } catch (Exception exception) {
      if (transaction.isActive()) transaction.rollback();
      System.out.println(exception.getMessage());
      throw new PersistenceException(exception);
    } finally {
      entityManager.close();
    }
  }

}
