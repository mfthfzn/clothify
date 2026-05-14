package com.github.mfthfzn.repository;

import com.github.mfthfzn.entity.TransactionItem;

import java.util.List;

public interface TransactionItemRepository {

  List<TransactionItem> findByTransactionId(Integer id);

}
