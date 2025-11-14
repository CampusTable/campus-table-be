package com.campustable.be.global.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate; // import 주의
import org.springframework.data.jpa.domain.support.AuditingEntityListener; // import 주의

@Getter
@MappedSuperclass // Entity들이 이 클래스의 필드를 컬럼으로 인식하도록 함
@EntityListeners(AuditingEntityListener.class) // Auditing 기능을 적용하기 위해 리스너 추가
public abstract class BaseTimeEntity {

  @CreatedDate // 엔티티가 생성될 때 시간이 자동 저장됨
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate // 엔티티가 수정될 때 시간이 자동 저장됨
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
