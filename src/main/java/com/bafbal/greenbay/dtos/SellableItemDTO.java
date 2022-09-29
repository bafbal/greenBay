package com.bafbal.greenbay.dtos;

public class SellableItemDTO extends ItemDTO {

  public SellableItemDTO() {
  }

  public SellableItemDTO(String itemName, String description, String photoUrl, Long startPrice, Long purchasePrice, Long sellerId) {
    super(itemName, description, photoUrl, startPrice, purchasePrice, sellerId);
  }
}
