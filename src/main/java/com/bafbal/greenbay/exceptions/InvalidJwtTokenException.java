package com.bafbal.greenbay.exceptions;

public class InvalidJwtTokenException extends GreenBayException{

  public InvalidJwtTokenException(String message) {
    super(message);
  }
}
