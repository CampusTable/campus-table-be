package com.campustable.be.domain.auth.provider;

import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
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
        .claim("exp", validity)
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
    } catch (SignatureException | MalformedJwtException | ExpiredJwtException |
             UnsupportedJwtException | IllegalArgumentException e) {
      log.error("JWT 토큰 검증 실패: {} - {}", e.getClass().getSimpleName(), e.getMessage());
      throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
    }
  }
}
