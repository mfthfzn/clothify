package com.github.mfthfzn.dto;

public class TransactionItemResponse {

  private String productSku;

  private String productName;

  private Integer price;

  private Integer quantity;

  private Long subtotal;

  public TransactionItemResponse(String productSku, String productName, Integer price, Integer quantity) {
    this.productSku = productSku;
    this.productName = productName;
    this.price = price;
    this.quantity = quantity;
    this.subtotal = (long) price * quantity;
  }

  public String getProductSku() {
    return productSku;
  }

  public void setProductSku(String productSku) {
    this.productSku = productSku;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Long getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(Long subtotal) {
    this.subtotal = subtotal;
  }
}
