package com.bafbal.greenbay.unit;

import com.bafbal.greenbay.configurations.TestConfiguration;
import com.bafbal.greenbay.dtos.AuthenticateResponseDTO;
import com.bafbal.greenbay.dtos.UsernamePasswordDTO;
import com.bafbal.greenbay.exceptions.GreenBayUserNotFoundException;
import com.bafbal.greenbay.exceptions.MissingCredentialsException;
import com.bafbal.greenbay.security.GreenBayUserDetails;
import com.bafbal.greenbay.security.JwtUtil;
import com.bafbal.greenbay.security.MyUserDetailsService;
import com.bafbal.greenbay.services.AuthenticationService;
import com.bafbal.greenbay.services.AuthenticationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestConfiguration.class)
public class AuthenticationServiceTest {

  @Autowired
  private BeanFactory beanFactory;

  private JwtUtil jwtUtil = Mockito.mock(JwtUtil.class);
  private MyUserDetailsService myUserDetailsService = Mockito.mock(MyUserDetailsService.class);
  private AuthenticationService authenticationService = new AuthenticationServiceImpl(jwtUtil, myUserDetailsService);

  private UsernamePasswordDTO usernamePasswordDtoWithNullUsername;
  private UsernamePasswordDTO usernamePasswordDtoWithBlankUsername;
  private UsernamePasswordDTO usernamePasswordDtoWithNullPassword;
  private UsernamePasswordDTO usernamePasswordDtoWithBlankPassword;
  private UsernamePasswordDTO usernamePasswordDtoWithBlankUsernameAndPassword;
  private UsernamePasswordDTO usernamePasswordDtoWithNullUsernameAndPassword;
  private UsernamePasswordDTO fooBarUsernamePasswordDTO;
  private GreenBayUserDetails fooBarGreenBayUserDetails;

  @BeforeEach
  public void getModels() {
    usernamePasswordDtoWithNullUsername = beanFactory.getBean("usernamePasswordDtoWithNullUsername", UsernamePasswordDTO.class);
    usernamePasswordDtoWithBlankUsername = beanFactory.getBean("usernamePasswordDtoWithBlankUsername", UsernamePasswordDTO.class);
    usernamePasswordDtoWithNullPassword = beanFactory.getBean("usernamePasswordDtoWithNullPassword", UsernamePasswordDTO.class);
    usernamePasswordDtoWithBlankPassword = beanFactory.getBean("usernamePasswordDtoWithBlankPassword", UsernamePasswordDTO.class);
    usernamePasswordDtoWithBlankUsernameAndPassword = beanFactory.getBean("usernamePasswordDtoWithBlankUsernameAndPassword",
        UsernamePasswordDTO.class);
    usernamePasswordDtoWithNullUsernameAndPassword = beanFactory.getBean("usernamePasswordDtoWithNullUsernameAndPassword",
        UsernamePasswordDTO.class);
    fooBarUsernamePasswordDTO = beanFactory.getBean("fooBarUsernamePasswordDTO",
        UsernamePasswordDTO.class);
    fooBarGreenBayUserDetails = beanFactory.getBean("fooBarGreenBayUserDetails",
        GreenBayUserDetails.class);
  }

  @Test
  public void validateCredentials_WhenUsernameIsNull_ThrowsException() {
    Throwable exception = Assertions.assertThrows(MissingCredentialsException.class,
        () -> authenticationService.validateCredentials(usernamePasswordDtoWithNullUsername));
    Assertions.assertEquals("Username is missing.", exception.getMessage());
  }

  @Test
  public void validateCredentials_WhenUsernameIsBlank_ThrowsException() {
    Throwable exception = Assertions.assertThrows(MissingCredentialsException.class,
        () -> authenticationService.validateCredentials(usernamePasswordDtoWithBlankUsername));
    Assertions.assertEquals("Username is missing.", exception.getMessage());
  }

  @Test
  public void validateCredentials_WhenPasswordIsNull_ThrowsException() {
    Throwable exception = Assertions.assertThrows(MissingCredentialsException.class,
        () -> authenticationService.validateCredentials(usernamePasswordDtoWithNullPassword));
    Assertions.assertEquals("Password is missing.", exception.getMessage());
  }

  @Test
  public void validateCredentials_WhenPasswordIsBlank_ThrowsException() {
    Throwable exception = Assertions.assertThrows(MissingCredentialsException.class,
        () -> authenticationService.validateCredentials(usernamePasswordDtoWithBlankPassword));
    Assertions.assertEquals("Password is missing.", exception.getMessage());
  }

  @Test
  public void validateCredentials_WhenUsernameAndPasswordAreNull_ThrowsException() {
    Throwable exception = Assertions.assertThrows(MissingCredentialsException.class,
        () -> authenticationService.validateCredentials(usernamePasswordDtoWithNullUsernameAndPassword));
    Assertions.assertEquals("Username and password are missing.", exception.getMessage());
  }

  @Test
  public void validateCredentials_WhenPasswordAndUsernameAreBlank_ThrowsException() {
    Throwable exception = Assertions.assertThrows(MissingCredentialsException.class,
        () -> authenticationService.validateCredentials(usernamePasswordDtoWithBlankUsernameAndPassword));
    Assertions.assertEquals("Username and password are missing.", exception.getMessage());
  }

  @Test
  public void getAuthenticationResponse_WhenUsernameExists_ReturnsDTO() {
    Mockito.when(myUserDetailsService.loadUserByUsername(fooBarUsernamePasswordDTO.getUsername())).thenReturn(fooBarGreenBayUserDetails);
    Mockito.when(jwtUtil.generateToken(fooBarGreenBayUserDetails)).thenReturn("thisIsAJwtTokenPurelyForTestPurposes");

    AuthenticateResponseDTO authenticateResponseDTO = authenticationService.getAuthenticationResponse(fooBarUsernamePasswordDTO);

    Assertions.assertEquals("thisIsAJwtTokenPurelyForTestPurposes", authenticateResponseDTO.getToken());
    Assertions.assertEquals(0, authenticateResponseDTO.getBalance());
  }

  @Test
  public void getAuthenticationResponse_WhenUsernameDoesNotExist_ThrowsException() {
    Mockito.when(myUserDetailsService.loadUserByUsername(fooBarUsernamePasswordDTO.getUsername()))
        .thenThrow(new GreenBayUserNotFoundException("No such user exists."));
    Throwable exception = Assertions.assertThrows(GreenBayUserNotFoundException.class,
        () -> authenticationService.getAuthenticationResponse(fooBarUsernamePasswordDTO));
    Assertions.assertEquals("No such user exists.",exception.getMessage());
  }
}
