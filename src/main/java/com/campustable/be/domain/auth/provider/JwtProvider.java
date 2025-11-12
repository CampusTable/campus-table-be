package com.campustable.be.domain.auth.provider;

import com.campustable.be.domain.User.entity.User;
import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Getter
public class JwtProvider {

  private final String secretKeyString;
  private final long expirationInMs;
  private final long refreshInMs;
  private final SecretKey secretKey;
  private final RedisTemplate<String, Object> redisTemplate;

  public JwtProvider(@Value("${jwt.secret}") String secretKeyString,
      @Value("${jwt.expiration-time}") long expirationInMs,
      @Value("${jwt.refresh-expiration-time}") long refreshInMs,
      RedisTemplate<String, Object> redisTemplate) {
    this.secretKeyString = secretKeyString;
    this.expirationInMs = expirationInMs;
    this.refreshInMs = refreshInMs;
    this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
    this.redisTemplate = redisTemplate;
  }

  public String createToken(User user) {

    Map<String, Object> claims;

    claims = setClaims(user, expirationInMs);

    return Jwts.builder()
        .claims(claims)
        .signWith(secretKey, Jwts.SIG.HS256)
        .compact();
  }

  public String createRefreshToken(User user) {

    Map<String, Object> claims;

    claims = setClaims(user, refreshInMs);

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
        log.error("JwtProvider메서드에서 에러호출 : jwt 파싱실패 혹은 만료 {}", e.getMessage());
      throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
    }
    if (id == null || id.isBlank()) {
      log.warn("JWT Subject 클레임이 비어 있습니다.");
      throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
    }

    return Long.valueOf(id);
  }

  public void validateToken(String token) {
    try {
      Jwts.parser().
          verifyWith(secretKey)
          .build()
          .parseSignedClaims(token);
      if (redisTemplate.hasKey(token)) {
        log.error("redis블랙리스트에 존재하는 토큰 사용 시도 감지 :{}",token);
        throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
      }
    } catch (ExpiredJwtException e) {
      log.error("토큰이 만료되었습니다. {}" , e.getMessage());
      throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
    } catch (SignatureException e) {
      log.error("서명이 올바르지않습니다. {}", e.getMessage());
      throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
    } catch (MalformedJwtException e) {
      log.error("잘못된 토큰 구조 {}", e.getMessage());
      throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
    }
  }

  public Long getExpiration(String token) {
    try {
      Claims claims = Jwts.parser().verifyWith(secretKey)
          .build()
          .parseSignedClaims(token)
          .getPayload();

      Long expiration = (Long) claims.get("iat");
      Long now = new Date().getTime();
      Long remainingTime = expiration - now;
      return remainingTime > 0 ? remainingTime : 0L;
    } catch (ExpiredJwtException e) {
      // expiration 0 이면 토큰상태가 어떻게되던 상관없이 같은 동작을 취할거기때문에
      return 0L;
    }
  }

  public void setBlackList(String token, Long expiration) {
    if (expiration > 0){
      redisTemplate.opsForValue().set(token," logout", expiration, TimeUnit.SECONDS);
      log.info("access Token 블랙리스트 등록완료. 남은시간 {}ms", expiration);
    }
  }

  private Map<String,Object> setClaims(User user, Long additionalMs){

    Map<String,Object> claims = new HashMap<>();

    Long nowMs = System.currentTimeMillis() / 1000;
    Long validMs = nowMs + additionalMs / 1000;

    claims.put("role", user.getRole());
    claims.put("id", user.getUserId());
    claims.put("iat", nowMs);
    claims.put("exp", validMs);

    return claims;
  }
}
