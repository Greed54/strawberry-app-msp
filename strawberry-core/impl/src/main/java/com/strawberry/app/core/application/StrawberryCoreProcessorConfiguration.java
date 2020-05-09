package com.strawberry.app.core.application;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore;
import org.axonframework.messaging.StreamableMessageSource;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.springboot.util.jpa.ContainerManagedEntityManagerProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StrawberryCoreProcessorConfiguration {

  @PersistenceContext
  EntityManager entityManager;

  final EventProcessingConfigurer configurer;

  public StrawberryCoreProcessorConfiguration(EventProcessingConfigurer configurer) {
    this.configurer = configurer;
  }

  @PostConstruct
  public void config() {
    configurer.registerTokenStore(configuration -> customTokenStore());
    configurer.registerTrackingEventProcessorConfiguration(configuration -> configuration());
  }

  @Bean
  public TokenStore customTokenStore() {
    ContainerManagedEntityManagerProvider entityManagerProvider = new ContainerManagedEntityManagerProvider();
    entityManagerProvider.setEntityManager(entityManager);

    return JpaTokenStore.builder()
        .entityManagerProvider(entityManagerProvider)
        .serializer(JacksonSerializer.defaultSerializer())
        .build();
  }

  public TrackingEventProcessorConfiguration configuration() {
    return TrackingEventProcessorConfiguration.forSingleThreadedProcessing()
        .andInitialTrackingToken(StreamableMessageSource::createHeadToken);
  }
}
