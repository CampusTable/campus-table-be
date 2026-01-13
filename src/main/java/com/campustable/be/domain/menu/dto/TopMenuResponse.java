package com.campustable.be.domain.menu.dto;

import com.campustable.be.domain.menu.entity.Menu;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TopMenuResponse {

  private Long rank;
  private MenuResponse menu;

  public static TopMenuResponse of(Long rank, Menu menu) {
    return TopMenuResponse.builder()
        .rank(rank)
        .menu(MenuResponse.from(menu))
        .build();
  }

}
