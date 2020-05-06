package com.strawberry.app.core.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.strawberry.app.common.aggregate.AbstractAggregate;
import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.cqengine.RepositoryFactory;
import com.strawberry.app.common.cqengine.indexedstore.IndexedStoreImpl;
import com.strawberry.app.core.application.store.InternalStoreBuilder;
import com.strawberry.app.core.context.utils.service.RepositoryService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(StrawberryCoreApplicationProperties.class)
public class StrawberryCoreApplicationStarter {

  @Bean
  public RepositoryService repositoryService(RepositoryFactory repositoryFactory) {
    return new RepositoryService(repositoryFactory);
  }

  @Bean
  public RepositoryFactory repositoryFactory(List<IndexedStoreImpl> indexedStores) {
    return new RepositoryFactory(indexedStores);
  }

  @Bean
  public DefaultBehaviorEngine defaultBehaviorEngine(RepositoryFactory repositoryFactory, List<Behavior> behaviors) {
    return new DefaultBehaviorEngine(repositoryFactory, behaviors);
  }

  @Bean
  public List<IndexedStoreImpl> indexedStores(List<AbstractAggregate> aggregates, ObjectMapper objectMapper,
      StrawberryCoreApplicationProperties applicationProperties) {
    return InternalStoreBuilder.buildStateStores(aggregates)
        .stream()
        .peek(indexedStore -> indexedStore.init(applicationProperties.getStateDir(), objectMapper))
        .collect(Collectors.toList());
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .findAndRegisterModules()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }
}
