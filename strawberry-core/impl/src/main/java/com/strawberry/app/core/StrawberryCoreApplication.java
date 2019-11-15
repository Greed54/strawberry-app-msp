package com.strawberry.app.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.strawberry.app"})
public class StrawberryCoreApplication {

  public static void main(String[] args) {
    SpringApplication.run(StrawberryCoreApplication.class, args);
  }
}
