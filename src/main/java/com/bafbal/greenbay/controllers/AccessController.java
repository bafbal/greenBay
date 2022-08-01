package com.bafbal.greenbay.controllers;

import com.bafbal.greenbay.dtos.SimpleErrorDTO;
import com.bafbal.greenbay.dtos.UsernamePasswordDTO;
import com.bafbal.greenbay.security.AuthenticationResponse;
import com.bafbal.greenbay.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccessController {

  private final AuthenticationManager authenticationManager;
  private final AuthenticationService authenticationService;

  @Autowired
  public AccessController(AuthenticationManager authenticationManager, AuthenticationService authenticationService) {
    this.authenticationManager = authenticationManager;
    this.authenticationService = authenticationService;
  }

  @RequestMapping(path = "/user/login", method = RequestMethod.POST)
  public ResponseEntity<?> authenticate(@RequestBody UsernamePasswordDTO usernamePasswordDTO) {
    authenticationService.validateCredentials(usernamePasswordDTO);
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(usernamePasswordDTO.getUsername(), usernamePasswordDTO.getPassword()));
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(400).body(new SimpleErrorDTO("Username or password is incorrect."));
    }
    return ResponseEntity.ok(new AuthenticationResponse(authenticationService.getJwtToken(usernamePasswordDTO)));
  }

}
