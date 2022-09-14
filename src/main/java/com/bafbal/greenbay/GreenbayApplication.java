package com.bafbal.greenbay;

import com.bafbal.greenbay.models.User;
import com.bafbal.greenbay.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GreenbayApplication implements CommandLineRunner {

  @Autowired
  private UserRepository userRepository;

  public static void main(String[] args) {
    SpringApplication.run(GreenbayApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {

//    userRepository.save(new User("foo", "bar"));
  }
}
