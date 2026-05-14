package com.github.mfthfzn.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

  @Id
  @Column(nullable = false, length = 16)
  private String sku;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Integer price;

  @Column(nullable = false, length = 50)
  private String category;

  @Column(nullable = false, length = 7)
  private String size;

  @Column(nullable = false, length = 25)
  private String color;

  @Column(nullable = false)
  private Integer quantity;

  @OneToMany(mappedBy = "product")
  private final List<TransactionItem> transactionItems = new ArrayList<>();

  public Product() {
  }

  public Product(String sku, String name, Integer price, String category, String size, String color, Integer quantity) {
    this.sku = sku;
    this.name = name;
    this.price = price;
    this.category = category;
    this.size = size;
    this.color = color;
    this.quantity = quantity;
  }

  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }
}
