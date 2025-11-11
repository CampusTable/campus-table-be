package com.campustable.be.domain.auth.provider;

import com.campustable.be.global.exception.CustomException;
import com.campustable.be.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
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
      if (redisTemplate.hasKey(token)) {
        log.error("redis블랙리스트에 존재하는 토큰 사용 시도 감지 :{}",token);
        throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
      }
    } catch (SignatureException | MalformedJwtException | ExpiredJwtException |
             UnsupportedJwtException | IllegalArgumentException e) {
      log.error("JWT 토큰 검증 실패: {} - {}", e.getClass().getSimpleName(), e.getMessage());
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
      return 0L;
    }
    catch (Exception e) {
      log.error("토큰에서 만료 시간 추출 실패: {}", e.getMessage());
      return 0L;
    }
  }

  public void setBlackList(String token, Long expiration) {
    if (expiration > 0){
      redisTemplate.opsForValue().set(token," logout", expiration, TimeUnit.MILLISECONDS);
      log.info("access Token 블랙리스트 등록완료. 남은시간 {}ms", expiration);
    }
  }
}
