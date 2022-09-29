package com.bafbal.greenbay.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bids")
public class Bid {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long price;

  @ManyToOne
  @JoinColumn(name = "item_id")
  private Item item;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  public Bid() {
  }

  public Bid(User user, Long price, Item item) {
    this.user = user;
    this.price = price;
    this.item = item;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Long getPrice() {
    return price;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  public Item getItem() {
    return item;
  }
}
