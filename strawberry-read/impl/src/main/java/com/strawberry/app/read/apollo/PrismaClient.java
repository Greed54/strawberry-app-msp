package com.strawberry.app.read.apollo;

import com.apollographql.apollo.ApolloCall.Callback;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.strawberry.app.read.application.StrawberryReadApplicationProperties;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrismaClient {

  Logger LOGGER = LoggerFactory.getLogger(PrismaClient.class);

  ApolloClient apolloClient;

  public PrismaClient(ApolloClient apolloClient) {
    this.apolloClient = apolloClient;
  }

  public void mutate(Mutation mutation) {
    apolloClient
        .mutate(mutation)
        .enqueue(new Callback() {
          @Override
          public void onResponse(@NotNull Response response) {
            if (Optional.ofNullable(response.data()).isPresent()) {
              LOGGER.info("Mutated: {}", Objects.requireNonNull(response.data()).toString());
            }
            if (!response.errors().isEmpty()) {
              LOGGER.warn(response.errors().toString());
            }
          }
          @Override
          public void onFailure(@NotNull ApolloException e) {
            LOGGER.error(e.getMessage());
          }
        });
  }
}
