package com.github.mfthfzn.service;

import com.github.mfthfzn.dto.TransactionRequest;
import com.github.mfthfzn.dto.TransactionResponse;
import com.github.mfthfzn.entity.Transaction;

import java.util.List;

public interface TransactionService {

  void create(TransactionRequest request);

  List<TransactionResponse> getByCustomerName(String customerName);

  List<TransactionResponse> getAll();

}
