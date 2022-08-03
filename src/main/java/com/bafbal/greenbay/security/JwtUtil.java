package com.bafbal.greenbay.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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
    return Long.parseLong(extractAllClaims(token).get("jti", String.class));
  }

  private Claims extractAllClaims(String token) {
    Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    return claims;
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
