package com.github.mfthfzn.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductRequest {

  @NotBlank(message = "SKU wajib diisi")
  @Size(max = 16, message = "SKU maksimal 16 karakter")
  private String sku;

  @NotBlank(message = "Nama produk tidak boleh kosong")
  @Size(max = 255, message = "Nama produk terlalu panjang")
  private String name;

  @NotNull(message = "Harga harus diisi")
  @Min(value = 10_000, message = "Harga minimal adalah 10.000")
  private Integer price;

  @NotBlank(message = "Kategori harus dipilih")
  private String category;

  @NotBlank(message = "Ukuran harus dipilih")
  @Size(max = 7, message = "Ukuran maksimal 7 karakter")
  private String size;

  @NotBlank(message = "Warna tidak boleh kosong")
  private String color;

  @NotNull(message = "Stok harus diisi")
  @Min(value = 0, message = "Stok tidak boleh negatif")
  private Integer quantity;

  public ProductRequest() {
  }

  public ProductRequest(String sku, String name, Integer price, String category, String size, String color, Integer quantity) {
    this.sku = sku;
    this.name = name;
    this.price = price;
    this.category = category;
    this.size = size;
    this.color = color;
    this.quantity = quantity;
  }

  public String getSku() { return sku; }
  public void setSku(String sku) { this.sku = sku; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public Integer getPrice() { return price; }
  public void setPrice(Integer price) { this.price = price; }

  public String getCategory() { return category; }
  public void setCategory(String category) { this.category = category; }

  public String getSize() { return size; }
  public void setSize(String size) { this.size = size; }

  public String getColor() { return color; }
  public void setColor(String color) { this.color = color; }

  public Integer getQuantity() { return quantity; }
  public void setQuantity(Integer quantity) { this.quantity = quantity; }
}