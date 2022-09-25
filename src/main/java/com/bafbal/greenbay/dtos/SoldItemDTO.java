package com.bafbal.greenbay.dtos;

public class SoldItemDTO extends ItemDTO {

  private Long buyerId;

  public SoldItemDTO() {
  }

  public Long getBuyerId() {

    return buyerId;
  }

  public void setBuyerId(Long buyerId) {
    this.buyerId = buyerId;
  }
}
