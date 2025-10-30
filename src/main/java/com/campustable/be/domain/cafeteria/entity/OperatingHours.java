package com.campustable.be.domain.cafeteria.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "operating_hours")
public class OperatingHours {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "operating_id")
  private Long operatingId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="cafeteria_id", nullable = false)
  private Cafeteria cafeteria;

  @Enumerated(EnumType.STRING)
  @Column(name = "day_of_week", nullable = false, length = 3)
  private DayOfWeekEnum dayOfWeek;

  @Column(name = "open_time", nullable = false)
  private LocalTime openTime;

  @Column(name = "close_time", nullable = false)
  private LocalTime closeTime;

}
