package com.bafbal.greenbay.services;

import com.bafbal.greenbay.dtos.UsernamePasswordDTO;
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
  }

  @Override
  public String getJwtToken(UsernamePasswordDTO usernamePasswordDTO) {
    return jwtUtil.generateToken(myUserDetailsService.loadUserByUsername(usernamePasswordDTO.getUsername()));
  }
}
