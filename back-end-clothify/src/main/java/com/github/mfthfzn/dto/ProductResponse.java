package com.github.mfthfzn.dto;

import com.github.mfthfzn.entity.Product;

public class ProductResponse {

  private String sku;
  private String name;
  private Integer price;
  private String category;
  private String size;
  private String color;
  private Integer quantity;

  public ProductResponse() {
  }

  public ProductResponse(Product product) {
    this.sku = product.getSku();
    this.name = product.getName();
    this.price = product.getPrice();
    this.category = product.getCategory();
    this.size = product.getSize();
    this.color = product.getColor();
    this.quantity = product.getQuantity();
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
