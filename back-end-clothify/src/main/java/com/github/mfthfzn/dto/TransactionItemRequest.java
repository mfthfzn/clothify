package com.github.mfthfzn.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TransactionItemRequest {

  @NotBlank(message = "SKU produk tidak boleh kosong")
  private String productSku;

  @NotNull(message = "Quantity tidak boleh kosong")
  @Min(value = 1, message = "Minimal pembelian adalah 1 item")
  private Integer quantity;

  public TransactionItemRequest() {
  }

  public TransactionItemRequest(Integer currentPrice, Integer quantity, String productSku) {
    this.quantity = quantity;
    this.productSku = productSku;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public String getProductSku() {
    return productSku;
  }

  public void setProductSku(String productSku) {
    this.productSku = productSku;
  }
}
