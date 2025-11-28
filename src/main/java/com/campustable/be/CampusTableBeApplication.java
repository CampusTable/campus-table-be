package com.campustable.be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CampusTableBeApplication {

  /**
   * Application entry point that boots the Spring application context and starts the CampusTable BE application.
   *
   * @param args command-line arguments passed to the application
   */
  public static void main(String[] args) {
    SpringApplication.run(CampusTableBeApplication.class, args);
  }

}