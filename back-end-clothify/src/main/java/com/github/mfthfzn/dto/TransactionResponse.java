package com.github.mfthfzn.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class TransactionResponse {

  private Integer id;

  private String customerName;

  private String customerPhoneNumber;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;

  private List<TransactionItemResponse> transactionItems;

  public TransactionResponse() {
  }

  public TransactionResponse(Integer id, String customerName,
                             String customerPhoneNumber,
                             LocalDateTime createdAt,
                             List<TransactionItemResponse> transactionItems) {
    this.id = id;
    this.customerName = customerName;
    this.customerPhoneNumber = customerPhoneNumber;
    this.createdAt = createdAt;
    this.transactionItems = transactionItems;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getCustomerPhoneNumber() {
    return customerPhoneNumber;
  }

  public void setCustomerPhoneNumber(String customerPhoneNumber) {
    this.customerPhoneNumber = customerPhoneNumber;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public List<TransactionItemResponse> getTransactionItems() {
    return transactionItems;
  }

  public void setTransactionItems(List<TransactionItemResponse> transactionItems) {
    this.transactionItems = transactionItems;
  }
}
