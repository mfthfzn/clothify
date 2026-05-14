package com.github.mfthfzn.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public class TransactionRequest {

  @NotBlank(message = "Nomor telepon tidak boleh kosong")
  @Size(min = 10, max = 15, message = "Nomor telepon harus antara 10-15 karakter")
  @Pattern(regexp = "^[0-9]*$", message = "Nomor telepon hanya boleh berisi angka")
  private String customerPhoneNumber;

  @NotBlank(message = "Nama pelanggan tidak boleh kosong")
  @Size(max = 150, message = "Nama pelanggan maksimal 150 karakter")
  private String customerName;

  @NotEmpty(message = "Keranjang belanja tidak boleh kosong")
  @Valid
  private List<TransactionItemRequest> transactionItems;

  public TransactionRequest() {
  }

  public TransactionRequest(String customerPhoneNumber, String customerName, List<TransactionItemRequest> transactionItems) {
    this.customerPhoneNumber = customerPhoneNumber;
    this.customerName = customerName;
    this.transactionItems = transactionItems;
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

  public List<TransactionItemRequest> getTransactionItems() {
    return transactionItems;
  }

  public void setTransactionItems(List<TransactionItemRequest> transactionItems) {
    this.transactionItems = transactionItems;
  }
}
