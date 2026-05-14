package com.github.mfthfzn.service;

import com.github.mfthfzn.dto.ProductRequest;
import com.github.mfthfzn.dto.ProductResponse;
import com.github.mfthfzn.entity.Product;
import com.github.mfthfzn.exception.ProductExistsException;
import com.github.mfthfzn.exception.ProductNotFoundException;
import com.github.mfthfzn.repository.ProductRepository;

import java.util.LinkedList;
import java.util.List;

public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  public ProductServiceImpl(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Override
  public void create(ProductRequest productRequest) {
    productRepository.findBySku(productRequest.getSku()).ifPresent(product -> {
      throw new ProductExistsException("Product with SKU " + productRequest.getSku() + " already exists");
    });

    Product product = new Product();
    product.setSku(productRequest.getSku());
    product.setName(productRequest.getName());
    product.setPrice(productRequest.getPrice());
    product.setCategory(productRequest.getCategory());
    product.setSize(productRequest.getSize());
    product.setColor(productRequest.getColor());
    product.setQuantity(productRequest.getQuantity());

    productRepository.insert(product);
  }

  @Override
  public ProductResponse getBySku(String sku) {
    Product product = productRepository.findBySku(sku)
            .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    return new ProductResponse(product);
  }

  @Override
  public void update(ProductRequest productRequest) {
    Product product = productRepository.findBySku(productRequest.getSku())
            .orElseThrow(() -> new ProductNotFoundException("Product not found"));

    product.setName(productRequest.getName());
    product.setPrice(productRequest.getPrice());
    product.setCategory(productRequest.getCategory());
    product.setSize(productRequest.getSize());
    product.setColor(productRequest.getColor());
    product.setQuantity(productRequest.getQuantity());

    productRepository.update(product);
  }

  @Override
  public void remove(String sku) {
    Product product = productRepository.findBySku(sku)
            .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    productRepository.remove(product);
  }

  @Override
  public List<ProductResponse> getAll() {
    List<Product> products = productRepository.getAll();

    List<ProductResponse> productResponses = new LinkedList<>();

    if (products.isEmpty()) return productResponses;

    products.forEach(product -> {
      productResponses.add(new ProductResponse(product));
    });

    return productResponses;

  }
}
