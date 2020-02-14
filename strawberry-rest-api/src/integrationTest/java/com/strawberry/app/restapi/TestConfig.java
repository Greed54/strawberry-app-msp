package com.strawberry.app.restapi;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class TestConfig {

  @Bean
  public CommandGateway commandGateway() {
    CommandGateway mock = mock(CommandGateway.class);
    when(mock.send(any())).thenReturn(CompletableFuture.completedFuture("sent"));
    return mock;
  }

}
