package com.bafbal.greenbay.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "items")
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String itemName;
  private String description;
  private String photoUrl;
  private Long startPrice;
  private Long purchasePrice;
  private Long lastBid;
  private boolean sold;

  @ManyToOne
  @JoinColumn(name = "buyer_id")
  private User buyer;

  @ManyToOne
  @JoinColumn(name = "seller_id")
  private User seller;

  public Item() {
  }

  public Item(String itemName, String description, String photoUrl, Long startPrice, Long purchasePrice, User seller) {
    this.itemName = itemName;
    this.description = description;
    this.photoUrl = photoUrl;
    this.startPrice = startPrice;
    this.purchasePrice = purchasePrice;
    this.seller = seller;
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

  public User getSeller() {
    return seller;
  }

  public Long getLastBid() {
    return lastBid;
  }
}
