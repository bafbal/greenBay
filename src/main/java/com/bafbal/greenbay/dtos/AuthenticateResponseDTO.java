package com.bafbal.greenbay.dtos;

public class AuthenticateResponseDTO {

  private String token;
  private Long balance;

  public AuthenticateResponseDTO(String token, Long balance) {
    this.token = token;
    this.balance = balance;
  }

  public String getToken() {
    return token;
  }

  public Long getBalance() {
    return balance;
  }
}
