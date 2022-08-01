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

  private String description;
  private String photoUrl;
  private Long startPrice;
  private Long purchasePrice;

  @ManyToOne
  @JoinColumn(name = "buyer_id")
  private User buyer;

  @ManyToOne
  @JoinColumn(name = "seller_id")
  private User seller;

  public Item() {
  }

  public Item(String description, String photoUrl, Long startPrice, User seller) {
    this.description = description;
    this.photoUrl = photoUrl;
    this.startPrice = startPrice;
    this.seller = seller;
  }
}
