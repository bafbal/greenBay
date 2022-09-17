package com.bafbal.greenbay;

import com.bafbal.greenbay.models.Item;
import com.bafbal.greenbay.models.User;
import com.bafbal.greenbay.repositories.ItemRepository;
import com.bafbal.greenbay.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GreenbayApplication implements CommandLineRunner {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ItemRepository itemRepository;

  public static void main(String[] args) {
    SpringApplication.run(GreenbayApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {

//    itemRepository.deleteAll();
//    userRepository.deleteAll();
//    User user = new User("foo", "bar");
//    userRepository.save(user);
//    itemRepository.save(new Item("first item", "first item description", "https://www.firstitem.com", 1l, 2l, user));
//    itemRepository.save(new Item("second item", "second item description", "https://www.seconditem.com", 2l, 4l, user));
//    itemRepository.save(new Item("third item", "third item description", "https://www.thirditem.com", 3l, 6l, user));
//    itemRepository.save(new Item("fourth item", "fourth item description", "https://www.fourthitem.com", 4l, 8l, user));
  }
}
