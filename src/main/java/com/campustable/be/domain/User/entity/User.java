package com.campustable.be.domain.User.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "student_number", nullable = false, unique = true, length = 20)
  private String studentNumber;

  @Column(nullable = false, length = 10)
  private String role;

  @Column(name = "user_name", nullable = false, length = 20)
  private String userName;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "set_profile", nullable = false)
  private Boolean setProfile = false;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  public User(String studentNumber, String role) {
    this.studentNumber = studentNumber;
    this.role = role;
  }
}
