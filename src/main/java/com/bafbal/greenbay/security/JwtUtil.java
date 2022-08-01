package com.bafbal.greenbay.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {

  private String secretKey = "ffjfmoijcmfej";

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  public String generateToken(GreenBayUserDetails greenBayUserDetails) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, greenBayUserDetails.getId(), greenBayUserDetails.getUsername());
  }

  private String createToken(Map<String, Object> claims, Long id, String username) {
    return Jwts.builder().setClaims(claims).setId(Long.toString(id)).claim("name", username).signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }
}
