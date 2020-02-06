package com.strawberry.app.core;

import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.spring.config.AxonConfiguration;
import org.axonframework.springboot.autoconfig.AxonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

  @Bean
  public EmbeddedEventStore eEventStore(@Qualifier(value = "myStorageEngine") EventStorageEngine storageEngine) {
    return EmbeddedEventStore.builder()
        .storageEngine(storageEngine)
        .build();
  }

  @Bean
  public EventStorageEngine myStorageEngine() {
    return new InMemoryEventStorageEngine();
  }
}
