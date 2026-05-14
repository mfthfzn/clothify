package com.github.mfthfzn.exception;

public class OutOfStockException extends RuntimeException {
  public OutOfStockException(String message) {
    super(message);
  }
}
