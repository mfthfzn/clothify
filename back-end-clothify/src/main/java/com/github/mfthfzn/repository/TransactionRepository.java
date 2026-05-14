package com.github.mfthfzn.repository;

import com.github.mfthfzn.entity.Transaction;

import java.util.List;

public interface TransactionRepository {

  void insert(Transaction transactionParam);

  List<Transaction> findByCustomerName(String name);

  List<Transaction> getAll();

}
