package com.github.mfthfzn.controller;

import com.github.mfthfzn.dto.ProductRequest;
import com.github.mfthfzn.dto.ProductResponse;
import com.github.mfthfzn.exception.ProductExistsException;
import com.github.mfthfzn.exception.ProductNotFoundException;
import com.github.mfthfzn.repository.ProductRepositoryImpl;
import com.github.mfthfzn.service.ProductService;
import com.github.mfthfzn.service.ProductServiceImpl;
import com.github.mfthfzn.util.JpaUtil;
import com.github.mfthfzn.util.ValidatorUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet("/api/products")
public class ProductController extends Controller {

  ProductService productService =
          new ProductServiceImpl(new ProductRepositoryImpl(JpaUtil.getEntityManagerFactory()));

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String sku = req.getParameter("sku");

    try {
      if (sku != null && !sku.isBlank()) {
        ProductResponse product = productService.getBySku(sku);
        sendSuccess(resp, HttpServletResponse.SC_OK, "Success get product", product);
      } else {
        List<ProductResponse> products = productService.getAll();
        sendSuccess(resp, HttpServletResponse.SC_OK, "Success get all products", products);
      }
    } catch (ProductNotFoundException productNotFoundException) {
      sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Failed get product", Map.of(
              "message", productNotFoundException.getMessage()
      ));
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      ProductRequest productRequest = objectMapper.readValue(req.getInputStream(), ProductRequest.class);

      Set<ConstraintViolation<Object>> constraintViolations = ValidatorUtil.validate(productRequest);

      if (!constraintViolations.isEmpty()) {
        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
          sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Data request invalid", Map.of(
                  "message", constraintViolation.getMessage()
          ));
          break;
        }
        return;
      }

      productService.create(productRequest);
      sendSuccess(resp, HttpServletResponse.SC_OK, "Success add product", Map.of(
              "message", "Product added successfully"));

    } catch (ProductExistsException productExistsException) {
      sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Failed add product", Map.of(
              "message", productExistsException.getMessage()
      ));
    }
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      ProductRequest productRequest = objectMapper.readValue(req.getInputStream(), ProductRequest.class);

      Set<ConstraintViolation<Object>> constraintViolations = ValidatorUtil.validate(productRequest);

      if (!constraintViolations.isEmpty()) {
        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
          sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Data request invalid", Map.of(
                  "message", constraintViolation.getMessage()
          ));
          break;
        }
        return;
      }

      productService.update(productRequest);
      sendSuccess(resp, HttpServletResponse.SC_OK, "Success update product", Map.of(
              "message", "Product updated successfully"));

    } catch (ProductNotFoundException productNotFoundException) {
      sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Failed add product", Map.of(
              "message", productNotFoundException.getMessage()
      ));
    }
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String sku = req.getParameter("sku");

    if (sku == null || sku.isBlank()) {
      sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Data request invalid", Map.of(
              "message", "SKU parameter is required"
      ));
      return;
    }

    try {
      productService.remove(sku);
      sendSuccess(resp, HttpServletResponse.SC_OK, "Success delete product", Map.of(
              "message", "Product deleted successfully"));
    } catch (ProductNotFoundException productNotFoundException) {
      sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Failed add product", Map.of(
              "message", productNotFoundException.getMessage()
      ));
    }
  }
}
