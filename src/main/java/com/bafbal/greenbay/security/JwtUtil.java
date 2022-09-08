package com.bafbal.greenbay.security;

import com.bafbal.greenbay.exceptions.InvalidJwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {

  private String secretKey = "ffjfmoijcmfej";

  public String extractUsername(String token) {
    return extractAllClaims(token).get("name", String.class);
  }

  public Long extractId(String token) {
    return extractAllClaims(token).get("jti", Long.class);
  }

  private Claims extractAllClaims(String token) {
    try {
      return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    } catch (MalformedJwtException e) {
      throw new InvalidJwtTokenException("Jwt token is invalid.");
    }
  }

  public Boolean validateToken(String token, GreenBayUserDetails userDetails) {
    String username = extractUsername(token);
    Long id = extractId(token);
    return (username.equals(userDetails.getUsername()) && id == userDetails.getId());
  }

  public String generateToken(GreenBayUserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("jti", userDetails.getId());
    claims.put("name", userDetails.getUsername());
    return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, secretKey).compact();
  }
}
