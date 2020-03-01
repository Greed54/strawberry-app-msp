package com.strawberry.app.read.application;

import com.apollographql.apollo.ApolloClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.strawberry.app.common.cqengine.RepositoryFactory;
import com.strawberry.app.common.cqengine.indexedstore.IndexedStoreImpl;
import com.strawberry.app.read.application.store.ExternalProjectionStoreBuilder;
import com.strawberry.app.read.context.utils.RepositoryService;
import java.util.List;
import java.util.stream.Collectors;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(StrawberryReadApplicationProperties.class)
public class StrawberryReadApplicationStarter {

  @Bean
  public RepositoryService repositoryService(RepositoryFactory repositoryFactory) {
    return new RepositoryService(repositoryFactory);
  }

  @Bean
  public RepositoryFactory repositoryFactory(List<IndexedStoreImpl> indexedStores) {
    return new RepositoryFactory(indexedStores);
  }

  @Bean
  public List<IndexedStoreImpl> indexedStores(ObjectMapper objectMapper, StrawberryReadApplicationProperties applicationProperties) {
    return List.of(
        ExternalProjectionStoreBuilder.buildStrawberryEmployeeProjectionStore(),
        ExternalProjectionStoreBuilder.buildStrawberryTeamProjectionStore(),
        ExternalProjectionStoreBuilder.buildStrawberryWorkDayProjectionStore()
    )
        .stream()
        .peek(indexedStore -> indexedStore.init(applicationProperties.getStateDir(), objectMapper))
        .collect(Collectors.toList());
  }

  @Bean
  public ApolloClient apolloClient(StrawberryReadApplicationProperties applicationProperties) {
    OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
    return ApolloClient.builder()
        .serverUrl(applicationProperties.getPrismaUrl())
        .okHttpClient(okHttpClient)
        .build();
  }

  @Primary
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .findAndRegisterModules()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

}
