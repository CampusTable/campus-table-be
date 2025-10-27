package com.campustable.be.global.properties;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "springdoc")
public record SpringDocProperties(
    List<Server> servers
) {

  public record Server(
      String url,
      String description
  ) {

  }

}
