package com.bafbal.greenbay.dtos;

public class ListSellableItemsDTO {

  private String itemName;
  private String photoUrl;
  private Long lastBid;

  public ListSellableItemsDTO(String itemName, String photoUrl, Long lastBid) {
    this.itemName = itemName;
    this.photoUrl = photoUrl;
    this.lastBid = lastBid;
  }

  public String getItemName() {
    return itemName;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public Long getLastBid() {
    return lastBid;
  }
}
