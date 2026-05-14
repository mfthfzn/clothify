package com.github.mfthfzn.service;

import com.github.mfthfzn.dto.ProductRequest;
import com.github.mfthfzn.dto.ProductResponse;

import java.util.List;

public interface ProductService {

  void create(ProductRequest productRequest);

  ProductResponse getBySku(String sku);

  void update(ProductRequest productRequest);

  void remove(String sku);

  List<ProductResponse> getAll();

}
