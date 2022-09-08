package com.bafbal.greenbay.dtos;

public class CreateItemDTO {

  private String itemName;
  private String description;
  private String photoUrl;
  private Double startPrice;
  private Double purchasePrice;

  public CreateItemDTO(String itemName, String description, String photoUrl, Double startPrice, Double purchasePrice) {
    this.itemName = itemName;
    this.description = description;
    this.photoUrl = photoUrl;
    this.startPrice = startPrice;
    this.purchasePrice = purchasePrice;
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

  public void setStartPrice(Double startPrice) {
    this.startPrice = startPrice;
  }

  public void setPurchasePrice(Double purchasePrice) {
    this.purchasePrice = purchasePrice;
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

  public Double getStartPrice() {
    return startPrice;
  }

  public Double getPurchasePrice() {
    return purchasePrice;
  }
}
