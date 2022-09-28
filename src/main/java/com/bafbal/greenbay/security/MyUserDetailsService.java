package com.bafbal.greenbay.security;

import com.bafbal.greenbay.exceptions.GreenBayUserNotFoundException;
import com.bafbal.greenbay.models.User;
import com.bafbal.greenbay.repositories.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

  private UserRepository userRepository;

  @Autowired
  public MyUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public GreenBayUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userOptional = userRepository.findByUsername(username);
    if (userOptional.isPresent()) {
      return new GreenBayUserDetails(userOptional.get());
    } else {
      throw new GreenBayUserNotFoundException("No such user exists.");
    }
  }

  public User getLoggedInUser() {
    GreenBayUserDetails greenBayUserDetails = (GreenBayUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return greenBayUserDetails.getUser();
  }
}
