package com.campustable.be.domain.auth.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

// 'refreshToken' 이라는 keyspace를 사용하는 Redis Hash 구조로 저장됨
@RedisHash(value = "refreshToken")
@Getter
@Setter
@Builder
public class RefreshToken {

  @Id
  private String token; // Refresh Token 값 자체를 키로 사용

  private Long userId; // 토큰의 주인이 되는 사용자 ID

  // TTL: 토큰의 만료 시간을 설정 (초 단위)
  @TimeToLive
  private Long expiration;
}