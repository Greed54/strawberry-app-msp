package com.strawberry.app.restapi;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.strawberry.app.restapi"})
public class StrawberryRestApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(StrawberryRestApiApplication.class, args);
  }
}
