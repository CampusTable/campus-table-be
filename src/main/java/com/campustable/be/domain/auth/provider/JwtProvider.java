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

  private final String secretKeyString;
  @Getter
  private final long expirationInMs;
  @Getter
  private final long refreshInMs;
  private final SecretKey secretKey;

  /**
   * Create a JwtProvider configured with the signing secret and token lifetimes.
   *
   * @param secretKeyString the HMAC secret used to sign and verify JWTs
   * @param expirationInMs  access token lifetime in milliseconds
   * @param refreshInMs     refresh token lifetime in milliseconds
   */
  public JwtProvider(@Value("${jwt.secret}") String secretKeyString,
      @Value("${jwt.expiration-time}") long expirationInMs,
      @Value("${jwt.refresh-expiration-time}") long refreshInMs
      ) {
    this.secretKeyString = secretKeyString;
    this.expirationInMs = expirationInMs;
    this.refreshInMs = refreshInMs;
    this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Create a signed JWT access token for the given user.
   *
   * @param user the authenticated user whose id and role are embedded in the token claims
   * @return the compact serialized access token (JWT)
   */
  public String createAccessToken(User user) {

    Map<String, Object> claims;

    claims = setClaims(user, expirationInMs);

    return Jwts.builder()
        .claims(claims)
        .signWith(secretKey, Jwts.SIG.HS256)
        .compact();
  }

  /**
   * Create a signed refresh JWT for the given user that includes the provided JWT ID.
   *
   * @param user the user for whom the refresh token is issued
   * @param jti  the JWT ID to set in the token's "jti" claim (used to identify or revoke the refresh token)
   * @return     the compact serialized refresh JWT string
   */
  public String createRefreshToken(User user, String jti) {

    Map<String, Object> claims;

    claims = setClaims(user, refreshInMs);
    claims.put("jti", jti);
    return Jwts.builder()
        .claims(claims)
        .signWith(secretKey, Jwts.SIG.HS256)
        .compact();
  }

  /**
   * Extracts the subject (user ID) from the given JWT and returns it as a Long.
   *
   * @param token the compact JWT string to parse and verify
   * @return the `sub` claim parsed as a Long
   * @throws CustomException if the token is invalid, malformed, or the subject claim is missing or blank (ErrorCode.JWT_INVALID)
   */
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
      log.warn("JWT Subject 클레임이 비어 있습니다.");
      throw new CustomException(ErrorCode.JWT_INVALID);
    }

    return Long.valueOf(id);
  }

  /**
     * Validates the integrity and signature of the provided JWT.
     *
     * @param token the compact JWT string to validate
     */
    public void validateToken(String token) {
    Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
    }

  /**
   * Builds JWT payload claims for the given user using the current time and a lifetime offset.
   *
   * @param user the user whose role and identifier are embedded in the claims
   * @param additionalMs lifetime to add to the current time, in milliseconds (used to compute the `exp` claim)
   * @return a map with keys: `"role"` (user role), `"sub"` (user id as a string), `"iat"` (issued-at time in seconds since epoch),
   *         and `"exp"` (expiration time in seconds since epoch)
   */
  private Map<String,Object> setClaims(User user, Long additionalMs){

    Map<String,Object> claims = new HashMap<>();

    Long nowMs = System.currentTimeMillis() / 1000;
    Long validMs = nowMs + additionalMs / 1000;

    claims.put("role", user.getRole());
    claims.put("sub", user.getUserId().toString());
    claims.put("iat", nowMs);
    claims.put("exp", validMs);

    return claims;
  }

  /**
   * Extracts the JWT ID (`jti`) claim from the provided refresh token.
   *
   * @param refreshToken the signed refresh JWT string
   * @return the `jti` claim value from the token
   * @throws CustomException with ErrorCode.JWT_INVALID if the token is invalid, cannot be parsed, or the `jti` claim is missing or blank
   */
  public String getJti(String refreshToken) {
    try{
      Claims claims = Jwts.parser().verifyWith(secretKey)
          .build()
          .parseSignedClaims(refreshToken)
          .getPayload();
      String jti = (String) claims.get("jti");
      if (jti == null || jti.isBlank()) {
        log.error("jti가 리프레시토큰에 유효하지않습니다.");
        throw new CustomException(ErrorCode.JWT_INVALID);
      }
      return jti;
    } catch (Exception e) {
      log.error("refreshToken이 유효하지 않습니다. {}",e.getMessage());
      throw new CustomException(ErrorCode.JWT_INVALID);
    }
  }
}