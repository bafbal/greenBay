package com.bafbal.greenbay.exceptions;

public class GreenBayException extends RuntimeException {

  private String message;

  public GreenBayException(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
