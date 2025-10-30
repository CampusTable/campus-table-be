package com.campustable.be.domain.cafeteria.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DayOfWeekEnum {

  MON("MON"),
  TUE("TUE"),
  WED("WED"),
  THU("THU"),
  FRI("FRI"),
  SAT("SAT"),
  SUN("SUN");

  private final String dayOfWeekCode;
}