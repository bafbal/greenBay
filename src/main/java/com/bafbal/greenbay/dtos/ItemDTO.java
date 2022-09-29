package com.bafbal.greenbay.dtos;

public class ItemDTO {

  private String itemName;
  private String description;
  private String photoUrl;
  private Long startPrice;
  private Long purchasePrice;
  private Long sellerId;

  public ItemDTO() {
  }

  public ItemDTO(String itemName, String description, String photoUrl, Long startPrice, Long purchasePrice, Long sellerId) {
    this.itemName = itemName;
    this.description = description;
    this.photoUrl = photoUrl;
    this.startPrice = startPrice;
    this.purchasePrice = purchasePrice;
    this.sellerId = sellerId;
  }

  public String getItemName() {
    return itemName;
  }

  public String getDescription() {
    return description;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public Long getStartPrice() {
    return startPrice;
  }

  public Long getPurchasePrice() {
    return purchasePrice;
  }

  public Long getSellerId() {
    return sellerId;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public void setStartPrice(Long startPrice) {
    this.startPrice = startPrice;
  }

  public void setPurchasePrice(Long purchasePrice) {
    this.purchasePrice = purchasePrice;
  }

  public void setSellerId(Long sellerId) {
    this.sellerId = sellerId;
  }
}
