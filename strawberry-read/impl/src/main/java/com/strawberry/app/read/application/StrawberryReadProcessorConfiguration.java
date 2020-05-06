package com.strawberry.app.read.application;

import com.strawberry.app.common.event.MspApplicationRunningEvent;
import com.strawberry.app.common.event.MspApplicationStartedEvent;
import com.strawberry.app.common.topology.AbstractTopology;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore;
import org.axonframework.messaging.StreamableMessageSource;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.springboot.util.jpa.ContainerManagedEntityManagerProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StrawberryReadProcessorConfiguration {

  @PersistenceContext
  EntityManager entityManager;

  final EventProcessingConfigurer configurer;
  final EventProcessingConfiguration processingConfiguration;
  final List<AbstractTopology> topologies;


  public StrawberryReadProcessorConfiguration(EventProcessingConfigurer configurer,
      EventProcessingConfiguration processingConfiguration, List<AbstractTopology> topologies) {
    this.configurer = configurer;
    this.processingConfiguration = processingConfiguration;
    this.topologies = topologies;
  }

  @PostConstruct
  public void config() {
    configurer.registerTokenStore(configuration -> customTokenStore());
    configurer.registerTrackingEventProcessorConfiguration(configuration -> configuration());
  }

  @EventListener
  public void applicationStarted(MspApplicationStartedEvent event) {
    topologies.forEach(abstractTopology ->
        processingConfiguration.eventProcessorByProcessingGroup(abstractTopology.topologyName(), TrackingEventProcessor.class)
            .ifPresent(TrackingEventProcessor::shutDown));
  }

  @EventListener
  public void applicationRunning(MspApplicationRunningEvent event) {
    topologies.forEach(abstractTopology ->
        processingConfiguration.eventProcessorByProcessingGroup(abstractTopology.topologyName(), TrackingEventProcessor.class)
            .ifPresent(TrackingEventProcessor::start));
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
