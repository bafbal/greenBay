package com.bafbal.greenbay.models;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String username;

  private String password;
  private Long balance;

  @OneToMany(mappedBy = "buyer")
  private List<Item> listOfBoughtItems;

  @OneToMany(mappedBy = "seller")
  private List<Item> listOfSoldItems;

  public User() {
  }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
    balance = 0L;
  }

  public User(Long id, String username, String password) {
    this(username, password);
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public Long getBalance() {
    return balance;
  }
}
