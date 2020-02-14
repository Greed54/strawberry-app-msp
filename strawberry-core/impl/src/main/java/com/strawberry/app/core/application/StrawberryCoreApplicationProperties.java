package com.strawberry.app.core.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ConfigurationProperties(prefix = "msp-strawberry-core")
public class StrawberryCoreApplicationProperties {

  @Value("${stateDir}")
  private String stateDir;
}
