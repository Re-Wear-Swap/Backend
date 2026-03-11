package com.rewear.rewear;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RewearApplication {
  public static void main(String[] args) {
    SpringApplication.run(RewearApplication.class, args);
  }
}
