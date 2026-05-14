package com.github.mfthfzn.repository;

import com.github.mfthfzn.entity.Transaction;
import jakarta.persistence.*;

import java.util.List;

public class TransactionRepositoryImpl implements TransactionRepository {

  private final EntityManagerFactory entityManagerFactory;

  public TransactionRepositoryImpl(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public void insert(Transaction transactionParam) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();

      entityManager.persist(transactionParam);

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
  public List<Transaction> findByCustomerName(String customerName) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();
      TypedQuery<Transaction> transactionTypedQuery = entityManager
              .createQuery("SELECT t FROM Transaction t WHERE t.customerName LIKE :customerName", Transaction.class)
              .setParameter("customerName", "%"+ customerName + "%");
      List<Transaction> resultList = transactionTypedQuery.getResultList();
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

  @Override
  public List<Transaction> getAll() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();
      TypedQuery<Transaction> transactionTypedQuery = entityManager.createQuery("SELECT t FROM Transaction t", Transaction.class);
      List<Transaction> resultList = transactionTypedQuery.getResultList();
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
