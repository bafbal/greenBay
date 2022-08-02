package com.bafbal.greenbay.services;

import com.bafbal.greenbay.dtos.AuthenticateResponseDTO;
import com.bafbal.greenbay.dtos.UsernamePasswordDTO;
import com.bafbal.greenbay.exceptions.MissingCredentialsException;
import com.bafbal.greenbay.security.GreenBayUserDetails;
import com.bafbal.greenbay.security.JwtUtil;
import com.bafbal.greenbay.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  private JwtUtil jwtUtil;
  private MyUserDetailsService myUserDetailsService;

  @Autowired
  public AuthenticationServiceImpl(JwtUtil jwtUtil, MyUserDetailsService myUserDetailsService) {
    this.jwtUtil = jwtUtil;
    this.myUserDetailsService = myUserDetailsService;
  }

  @Override
  public void validateCredentials(UsernamePasswordDTO usernamePasswordDTO) {
    if ((usernamePasswordDTO.getUsername() == null || usernamePasswordDTO.getUsername().isBlank()) &&
        (usernamePasswordDTO.getPassword() == null || usernamePasswordDTO.getPassword().isBlank())) {
      throw new MissingCredentialsException("Username and password are missing.");
    }
    if (usernamePasswordDTO.getPassword() == null || usernamePasswordDTO.getPassword().isBlank()) {
      throw new MissingCredentialsException("Password is missing.");
    }
    if (usernamePasswordDTO.getUsername() == null || usernamePasswordDTO.getUsername().isBlank()) {
      throw new MissingCredentialsException("Username is missing.");
    }
  }

  @Override
  public AuthenticateResponseDTO getAuthenticationResponse(UsernamePasswordDTO usernamePasswordDTO) {
    GreenBayUserDetails greenBayUserDetails = myUserDetailsService.loadUserByUsername(usernamePasswordDTO.getUsername());
    return new AuthenticateResponseDTO(jwtUtil.generateToken(greenBayUserDetails), greenBayUserDetails.getBalance());
  }
}
