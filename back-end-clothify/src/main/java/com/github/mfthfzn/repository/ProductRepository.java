package com.github.mfthfzn.repository;

import com.github.mfthfzn.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

  void insert(Product product);

  Optional<Product> findBySku(String name);

  void update(Product product);

  void remove(Product product);

  List<Product> getAll();

}
