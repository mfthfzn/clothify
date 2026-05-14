package com.github.mfthfzn.repository;

import com.github.mfthfzn.entity.Product;
import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository {

  private final EntityManagerFactory entityManagerFactory;

  public ProductRepositoryImpl(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public void insert(Product product) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();

      entityManager.persist(product);

      transaction.commit();

    } catch (Exception exception) {
      if (transaction.isActive()) transaction.rollback();
      System.out.println(exception.getMessage());
      throw new PersistenceException(exception);
    } finally {
      entityManager.close();
    }
  }

  @Override
  public Optional<Product> findBySku(String sku) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();
      Product product = entityManager.find(Product.class, sku);
      transaction.commit();
      return Optional.ofNullable(product);
    } catch (Exception exception) {
      if (transaction.isActive()) transaction.rollback();
      System.out.println(exception.getMessage());
      throw new PersistenceException(exception);
    } finally {
      entityManager.close();
    }
  }

  @Override
  public void update(Product product) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();
      entityManager.merge(product);
      transaction.commit();
    } catch (Exception exception) {
      if (transaction.isActive()) transaction.rollback();
      System.out.println(exception.getMessage());
      throw new PersistenceException(exception);
    } finally {
      entityManager.close();
    }
  }

  @Override
  public void remove(Product product) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {

      transaction.begin();
      Product reference = entityManager.getReference(Product.class, product.getSku());
      entityManager.remove(reference);
      transaction.commit();

    } catch (Exception exception) {
      if (transaction.isActive()) transaction.rollback();
      System.out.println(exception.getMessage());
      throw new PersistenceException(exception);
    } finally {
      entityManager.close();
    }
  }

  @Override
  public List<Product> getAll() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {

      transaction.begin();
      TypedQuery<Product> products = entityManager.createQuery("SELECT p FROM Product p", Product.class);
      List<Product> resultList = products.getResultList();
      transaction.commit();

      return resultList;

    } catch (Exception exception) {
      if (transaction.isActive()) transaction.rollback();
      System.out.println(exception.getMessage());
      throw new PersistenceException(exception);
    } finally {
      entityManager.close();
    }
  }
}
