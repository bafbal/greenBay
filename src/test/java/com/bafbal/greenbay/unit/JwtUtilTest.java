package com.bafbal.greenbay.unit;

import com.bafbal.greenbay.exceptions.InvalidJwtTokenException;
import com.bafbal.greenbay.security.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JwtUtilTest {

  private JwtUtil jwtUtil = new JwtUtil();

  @Test
  public void extractUsername_WhenValidTokenIsGiven_ReturnsUsername() {
    Assertions.assertEquals("foo",
        jwtUtil.extractUsername("eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiZm9vIiwianRpIjoxfQ.Zm5-22i3u96d9nGotuLGIWL6-7Kh9JGpMGsoL8NgPxg"));
  }

  @Test
  public void extractUsername_WhenInValidTokenIsGiven_ThrowsException() {
    Throwable exception = Assertions.assertThrows(InvalidJwtTokenException.class, () -> jwtUtil.extractUsername(
        "feyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiZm9vIiwianRpIjoxfQ.Zm5-22i3u96d9nGotuLGIWL6-7Kh9JGpMGsoL8NgPxg"));
    Assertions.assertEquals("Jwt token is invalid.", exception.getMessage());
  }

  @Test
  public void extractId_WhenValidTokenIsGiven_ReturnsId() {
    Assertions.assertEquals(1,
        jwtUtil.extractId("eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiZm9vIiwianRpIjoxfQ.Zm5-22i3u96d9nGotuLGIWL6-7Kh9JGpMGsoL8NgPxg"));
  }

  @Test
  public void extractId_WhenInValidTokenIsGiven_ThrowsException() {
    Throwable exception = Assertions.assertThrows(InvalidJwtTokenException.class, () -> jwtUtil.extractId(
        "feyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiZm9vIiwianRpIjoxfQ.Zm5-22i3u96d9nGotuLGIWL6-7Kh9JGpMGsoL8NgPxg"));
    Assertions.assertEquals("Jwt token is invalid.", exception.getMessage());
  }
}
