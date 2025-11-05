package com.campustable.be.domain.auth.provider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Getter
public class JwtProvider {

  private final String secretKeyString;

  private final long expirationInMs;

  private final long refreshInMs;

  private final SecretKey secretKey;

  public JwtProvider(@Value("${jwt.secret}") String secretKeyString,
      @Value("${jwt.expiration-time}") long expirationInMs,
      @Value("${jwt.refresh-expiration-time}") long refreshInMs) {
    this.secretKeyString = secretKeyString;
    this.expirationInMs = expirationInMs;
    this.refreshInMs = refreshInMs;
    this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));

  }

  public String createToken(String subject, Map<String, Object> claims) {

    Date now = new Date();
    Date validity = new Date(now.getTime() + expirationInMs);

    claims.put("sub", subject);
    claims.put("iat", now);
    claims.put("exp", validity);

    return Jwts.builder()
        .claims(claims)
        .signWith(secretKey, Jwts.SIG.HS256)
        .compact();
  }

  public String createRefreshToken(String subject){

    Date now = new Date();
    Date validity = new Date(now.getTime() +  refreshInMs);

    return Jwts.builder()
        .claim("sub", subject)
        .claim("iat", now)
        .signWith(secretKey, SIG.HS256)
        .compact();
  }

  public String getSubject(String token){
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public void validateToken(String token) {

    try {
      Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
    } catch (io.jsonwebtoken.security.SignatureException e) {
      log.error("Invalid JWT signature: {}", e.getMessage());
    } catch (io.jsonwebtoken.MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    } catch (io.jsonwebtoken.ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
    } catch (io.jsonwebtoken.UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
    }
  }
}
