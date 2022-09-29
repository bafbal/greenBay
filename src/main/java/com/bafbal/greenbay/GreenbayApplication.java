package com.bafbal.greenbay;

import com.bafbal.greenbay.models.Item;
import com.bafbal.greenbay.models.User;
import com.bafbal.greenbay.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GreenbayApplication implements CommandLineRunner {

  private UserRepository userRepository;

  @Autowired
  public GreenbayApplication(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public static void main(String[] args) {
    SpringApplication.run(GreenbayApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
//    User user = new User("foo", "bar");
//    user.addToBalance(100l);
//    userRepository.save(user);
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
