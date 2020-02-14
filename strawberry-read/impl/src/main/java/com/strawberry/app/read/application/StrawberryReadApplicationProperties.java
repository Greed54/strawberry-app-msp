package com.strawberry.app.read.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("msp-strawberry-read")
public class StrawberryReadApplicationProperties {

  private String prismaUrl;

  private String stateDir;
}
