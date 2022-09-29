package com.bafbal.greenbay.dtos;

public class SoldItemDTO extends ItemDTO {

  private Long buyerId;

  public SoldItemDTO() {
  }

  public SoldItemDTO(String itemName, String description, String photoUrl, Long startPrice, Long purchasePrice, Long sellerId,
      Long buyerId) {
    super(itemName, description, photoUrl, startPrice, purchasePrice, sellerId);
    this.buyerId = buyerId;
  }

  public Long getBuyerId() {

    return buyerId;
  }

  public void setBuyerId(Long buyerId) {
    this.buyerId = buyerId;
  }
}
