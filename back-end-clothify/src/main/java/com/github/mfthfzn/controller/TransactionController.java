package com.github.mfthfzn.controller;

import com.github.mfthfzn.dto.TransactionRequest;
import com.github.mfthfzn.dto.TransactionResponse;
import com.github.mfthfzn.exception.OutOfStockException;
import com.github.mfthfzn.exception.ProductNotFoundException;
import com.github.mfthfzn.exception.TransactionNotFoundException;
import com.github.mfthfzn.repository.ProductRepositoryImpl;
import com.github.mfthfzn.repository.TransactionRepositoryImpl;
import com.github.mfthfzn.service.TransactionService;
import com.github.mfthfzn.service.TransactionServiceImpl;
import com.github.mfthfzn.util.JpaUtil;
import com.github.mfthfzn.util.ValidatorUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TransactionController extends Controller {

  private final TransactionService transactionService =
          new TransactionServiceImpl(
                  new TransactionRepositoryImpl(JpaUtil.getEntityManagerFactory()),
                  new ProductRepositoryImpl(JpaUtil.getEntityManagerFactory())
          );

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    try {
      String customerName = req.getParameter("name");
      List<TransactionResponse> responses;

      if (customerName != null && !customerName.isEmpty()) {
        responses = transactionService.getByCustomerName(customerName);
      } else {
        responses = transactionService.getAll();
      }

      sendSuccess(resp, HttpServletResponse.SC_OK, "Success get transaction", responses);

    } catch (TransactionNotFoundException transactionNotFoundException) {
      sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Failed get transaction", Map.of(
              "message", transactionNotFoundException.getMessage()
      ));
    }

  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {

      TransactionRequest transactionRequest = objectMapper.readValue(req.getReader(), TransactionRequest.class);

      Set<ConstraintViolation<Object>> constraintViolations = ValidatorUtil.validate(transactionRequest);

      if (!constraintViolations.isEmpty()) {
        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
          sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Data request invalid", Map.of(
                  "message", constraintViolation.getMessage()
          ));
          break;
        }
        return;
      }

      transactionService.create(transactionRequest);
      sendSuccess(resp, HttpServletResponse.SC_CREATED, "Success create transaction", Map.of(
              "message", "Transaction created successfully"));

    } catch (OutOfStockException outOfStockException) {
      sendError(resp, 422, "Failed create transaction", Map.of(
              "message", outOfStockException.getMessage()
      ));
    } catch (ProductNotFoundException productNotFoundException) {
      sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Failed found product", Map.of(
              "message", productNotFoundException.getMessage()
      ));
    }
  }

}
