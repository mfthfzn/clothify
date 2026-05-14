package com.github.mfthfzn.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "transaction_items")
public class TransactionItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "current_price", nullable = false)
  private Integer currentPrice;

  @Column(nullable = false)
  private Integer quantity;

  @ManyToOne
  @JoinColumn(
          name = "transaction_id",
          referencedColumnName = "id"
  )
  private Transaction transaction;

  @ManyToOne
  @JoinColumn(
          name = "product_sku",
          referencedColumnName = "sku"
  )
  private Product product;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getCurrentPrice() {
    return currentPrice;
  }

  public void setCurrentPrice(Integer currentPrice) {
    this.currentPrice = currentPrice;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Transaction getTransaction() {
    return transaction;
  }

  public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }
}
