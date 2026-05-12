package com.github.mfthfzn.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebResponse<T> {

  private String message;

  private T data;

  private Object error;

  public WebResponse(String message, T data, Object error) {
    this.message = message;
    this.data = data;
    this.error = error;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public Object getError() {
    return error;
  }

  public void setError(Object error) {
    this.error = error;
  }
}