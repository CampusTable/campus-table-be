package com.campustable.be.domain.cafeteria.entity;


import com.campustable.be.domain.cafeteria.dto.OperatingHoursRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "operating_hours")
public class OperatingHours {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "operating_id")
  private Long operatingId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cafeteria_id", nullable = false,
      foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT,
          foreignKeyDefinition = "FOREIGN KEY (cafeteria_id) REFERENCES cafeterias (cafeteria_id) ON DELETE CASCADE"))
  private Cafeteria cafeteria;

  @Enumerated(EnumType.STRING)
  @Column(name = "day_of_week", nullable = false, length = 3)
  private DayOfWeekEnum dayOfWeek;

  @Column(name = "break_open_time")
  @JsonFormat(pattern="HH:mm")
  private LocalTime breaksStartTime;

  @Column(name = "break_close_time")
  @JsonFormat(pattern="HH:mm")
  private LocalTime breaksCloseTime;

  @Column(name = "open_time", nullable = false)
  @JsonFormat(pattern="HH:mm")
  private LocalTime openTime;

  @Column(name = "close_time", nullable = false)
  @JsonFormat(pattern="HH:mm")
  private LocalTime closeTime;

  public void update(OperatingHoursRequest request) {
    this.dayOfWeek = request.getDayOfWeek();
    this.breaksStartTime = request.getBreaksStartTime();
    this.breaksCloseTime = request.getBreaksCloseTime();
    this.openTime = request.getOpenTime();
    this.closeTime = request.getCloseTime();
  }
}
