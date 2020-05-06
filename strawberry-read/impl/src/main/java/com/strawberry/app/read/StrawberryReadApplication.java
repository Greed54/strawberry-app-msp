package com.strawberry.app.read;

import com.strawberry.app.read.application.StrawberryReadApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {"com.strawberry.app.read", "com.strawberry.app.common"})
@EnableConfigurationProperties(StrawberryReadApplicationProperties.class)
public class StrawberryReadApplication {

  public static void main(String[] args) {
    SpringApplication.run(StrawberryReadApplication.class, args);
  }
}
