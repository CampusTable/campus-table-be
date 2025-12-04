package com.campustable.be.domain.user.entity;

import com.campustable.be.domain.user.dto.UserRequest;
import com.campustable.be.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "student_number", unique = true, length = 20)
  private String studentNumber;

  @Column(nullable = false, length = 10)
  private String role = "USER";

  @Column(name = "user_name", length = 20)
  private String userName;

  @Column(name = "login_id", length = 20, unique = true)
  private String loginId;

  @Column(name = "password", length = 100)
  private String password = null;

  //디테일 나중에 처리할게요 비밀번호 해싱도해야함
  public void update(UserRequest userRequest) {
    this.password = userRequest.getPassword();
  }
}
