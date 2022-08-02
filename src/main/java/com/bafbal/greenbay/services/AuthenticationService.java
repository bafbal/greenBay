package com.bafbal.greenbay.services;

import com.bafbal.greenbay.dtos.AuthenticateResponseDTO;
import com.bafbal.greenbay.dtos.UsernamePasswordDTO;

public interface AuthenticationService {

  void validateCredentials(UsernamePasswordDTO usernamePasswordDTO);

  AuthenticateResponseDTO getAuthenticationResponse(UsernamePasswordDTO usernamePasswordDTO);
}
