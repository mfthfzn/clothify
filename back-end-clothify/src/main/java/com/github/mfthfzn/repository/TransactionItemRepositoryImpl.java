package com.github.mfthfzn.repository;

import com.github.mfthfzn.entity.TransactionItem;
import jakarta.persistence.*;

import java.util.List;

public class TransactionItemRepositoryImpl implements TransactionItemRepository {

  private final EntityManagerFactory entityManagerFactory;

  public TransactionItemRepositoryImpl(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public List<TransactionItem> findByTransactionId(Integer id) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();
      TypedQuery<TransactionItem> transactionItemTypedQuery = entityManager
              .createQuery("SELECT ti FROM TransactionItem ti WHERE ti.transaction.id = :transactionId", TransactionItem.class)
              .setParameter(1, id);
      List<TransactionItem> resultList = transactionItemTypedQuery.getResultList();
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
