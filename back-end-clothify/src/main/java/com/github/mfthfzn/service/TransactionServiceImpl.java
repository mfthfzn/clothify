package com.github.mfthfzn.service;

import com.github.mfthfzn.dto.TransactionItemRequest;
import com.github.mfthfzn.dto.TransactionItemResponse;
import com.github.mfthfzn.dto.TransactionRequest;
import com.github.mfthfzn.dto.TransactionResponse;
import com.github.mfthfzn.entity.Product;
import com.github.mfthfzn.entity.Transaction;
import com.github.mfthfzn.entity.TransactionItem;
import com.github.mfthfzn.exception.OutOfStockException;
import com.github.mfthfzn.exception.ProductNotFoundException;
import com.github.mfthfzn.exception.TransactionNotFoundException;
import com.github.mfthfzn.repository.ProductRepository;
import com.github.mfthfzn.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;

  private final ProductRepository productRepository;

  public TransactionServiceImpl(TransactionRepository transactionRepository, ProductRepository productRepository) {
    this.transactionRepository = transactionRepository;
    this.productRepository = productRepository;
  }

  @Override
  public void create(TransactionRequest request) {

    Transaction transaction = new Transaction();

    transaction.setCustomerName(request.getCustomerName());
    transaction.setCustomerPhoneNumber(request.getCustomerPhoneNumber());
    transaction.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

    for (TransactionItemRequest itemRequest : request.getTransactionItems()) {

      Product product = productRepository
              .findBySku(itemRequest.getProductSku())
              .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));

      if (product.getQuantity() <= itemRequest.getQuantity()) {
        throw new OutOfStockException(
                "Product stock " + product.getName() + " is insufficient. Remaining: " + product.getQuantity()
        );
      }

      product.setQuantity(product.getQuantity() - itemRequest.getQuantity());
      productRepository.update(product);

      TransactionItem transactionItem = new TransactionItem();
      transactionItem.setProduct(product);
      transactionItem.setCurrentPrice(product.getPrice());
      transactionItem.setQuantity(itemRequest.getQuantity());

      transaction.getTransactionItems().add(transactionItem);

    }

    transactionRepository.insert(transaction);

  }

  @Override
  public List<TransactionResponse> getByCustomerName(String customerName) {
    List<Transaction> transactions = transactionRepository.findByCustomerName(customerName);
    if (transactions.isEmpty()) throw new TransactionNotFoundException("Transaction not found");

    List<TransactionResponse> responses = new LinkedList<>();

    for (Transaction transaction : transactions) {

      List<TransactionItemResponse> transactionItemResponses = getTransactionItemResponses(transaction);

      TransactionResponse transactionResponse = new TransactionResponse();
      transactionResponse.setId(transaction.getId());
      transactionResponse.setCustomerName(transaction.getCustomerName());
      transactionResponse.setCustomerPhoneNumber(transaction.getCustomerPhoneNumber());
      transactionResponse.setCreatedAt(transaction.getCreatedAt());
      transactionResponse.setTransactionItems(transactionItemResponses);

      responses.add(transactionResponse);

    }

    return responses;
  }

  @Override
  public List<TransactionResponse> getAll() {
    List<Transaction> transactions = transactionRepository.getAll();

    List<TransactionResponse> responses = new LinkedList<>();
    if (transactions.isEmpty()) {
      return responses;
    }

    for (Transaction transaction : transactions) {

      List<TransactionItemResponse> transactionItemResponses = getTransactionItemResponses(transaction);

      TransactionResponse transactionResponse = new TransactionResponse();
      transactionResponse.setId(transaction.getId());
      transactionResponse.setCustomerName(transaction.getCustomerName());
      transactionResponse.setCustomerPhoneNumber(transaction.getCustomerPhoneNumber());
      transactionResponse.setCreatedAt(transaction.getCreatedAt());
      transactionResponse.setTransactionItems(transactionItemResponses);

      responses.add(transactionResponse);

    }

    return responses;
  }

  private static List<TransactionItemResponse> getTransactionItemResponses(Transaction transaction) {
    List<TransactionItemResponse> transactionItemResponses = new LinkedList<>();
    List<TransactionItem> transactionItems = transaction.getTransactionItems();

    for (TransactionItem transactionItem : transactionItems) {

      TransactionItemResponse itemResponse = new TransactionItemResponse(
              transactionItem.getProduct().getSku(),
              transactionItem.getProduct().getName(),
              transactionItem.getCurrentPrice(),
              transactionItem.getQuantity()
      );

      transactionItemResponses.add(itemResponse);

    }
    return transactionItemResponses;
  }
}
