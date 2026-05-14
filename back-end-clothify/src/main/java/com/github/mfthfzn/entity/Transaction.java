package com.github.mfthfzn.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transactions")
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "customer_phone_number", length = 15, nullable = false)
  private String customerPhoneNumber;

  @Column(name = "customer_name", nullable = false)
  private String customerName;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "transaction", cascade = CascadeType.PERSIST)
  private final List<TransactionItem> transactionItems = new ArrayList<>();

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCustomerPhoneNumber() {
    return customerPhoneNumber;
  }

  public void setCustomerPhoneNumber(String customerPhoneNumber) {
    this.customerPhoneNumber = customerPhoneNumber;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public List<TransactionItem> getTransactionItems() {
    return transactionItems;
  }
}
