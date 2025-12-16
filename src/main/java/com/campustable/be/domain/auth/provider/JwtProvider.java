package com.campustable.be.domain.auth.provider;

import com.campustable.be.domain.user.entity.User;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProvider {

  @Getter
  private final long expirationInMs;
  @Getter
  private final long refreshInMs;
  private final SecretKey secretKey;

  public JwtProvider(@Value("${jwt.secret}") String secretKeyString,
      @Value("${jwt.expiration-time}") long expirationInMs,
      @Value("${jwt.refresh-expiration-time}") long refreshInMs
      ) {
    this.expirationInMs = expirationInMs;
    this.refreshInMs = refreshInMs;
    this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
  }

  public String createAccessToken(User user) {

    Map<String, Object> claims;

    claims = setClaims(user, expirationInMs);

    return Jwts.builder()
        .claims(claims)
        .signWith(secretKey, Jwts.SIG.HS256)
        .compact();
  }

  public String createRefreshToken(User user, String jti) {

    Map<String, Object> claims;

    claims = setClaims(user, refreshInMs);
    claims.put("jti", jti);
    return Jwts.builder()
        .claims(claims)
        .signWith(secretKey, Jwts.SIG.HS256)
        .compact();
  }

  public Long getSubject(String token){

    String id;
    try {
      id = Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token)
          .getPayload()
          .getSubject();
    } catch (JwtException e) {
        log.error("유효하지않거나 잘못된형식의 jwt {}", e.getMessage());
      throw new CustomException(ErrorCode.JWT_INVALID);
    }
    if (id == null || id.isBlank()) {
      log.error("JWT Subject 클레임이 비어 있습니다.");
      throw new CustomException(ErrorCode.JWT_INVALID);
    }

    return Long.valueOf(id);
  }

  public void validateToken(String token) {
    Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
    }

  private Map<String,Object> setClaims(User user, long additionalMs){

    Map<String,Object> claims = new HashMap<>();

    long nowMs = System.currentTimeMillis();
    long validMs = nowMs + additionalMs;

    claims.put("role", user.getRole());
    claims.put("sub", user.getUserId().toString());
    claims.put("iat", nowMs / 1000);
    claims.put("exp", validMs / 1000);

    return claims;
  }

  public String getJti(String refreshToken) {
    try{
      Claims claims = Jwts.parser().verifyWith(secretKey)
          .build()
          .parseSignedClaims(refreshToken)
          .getPayload();
      String jti = (String) claims.get("jti");
      if (jti == null || jti.isBlank()) {
        log.error("jti가 리프레시토큰에 유효하지않습니다.");
        throw new CustomException(ErrorCode.REFRESH_TOKEN_INVALID);
      }
      return jti;
    } catch (Exception e) {
      log.error("refreshToken이 유효하지 않습니다. {}",e.getMessage());
      throw new CustomException(ErrorCode.REFRESH_TOKEN_INVALID);
    }
  }
}