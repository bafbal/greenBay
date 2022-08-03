package com.bafbal.greenbay.dtos;

public class SavedItemDTO {

  private Long id;
  private String itemName;
  private String description;
  private String photoUrl;
  private Long startPrice;
  private Long purchasePrice;
  private Long sellerId;

  public SavedItemDTO(Long id, String itemName, String description, String photoUrl, Long startPrice, Long purchasePrice,
      Long sellerId) {
    this.id = id;
    this.itemName = itemName;
    this.description = description;
    this.photoUrl = photoUrl;
    this.startPrice = startPrice;
    this.purchasePrice = purchasePrice;
    this.sellerId = sellerId;
  }

  public Long getId() {
    return id;
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
}
