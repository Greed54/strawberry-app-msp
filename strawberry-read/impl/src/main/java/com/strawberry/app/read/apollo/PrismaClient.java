package com.strawberry.app.read.apollo;

import com.apollographql.apollo.ApolloClient;
import com.strawberry.app.read.application.StrawberryReadApplicationProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrismaClient {

  @Getter
  ApolloClient apolloClient;

  public PrismaClient(StrawberryReadApplicationProperties properties) {
    OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
    apolloClient = ApolloClient.builder()
        .serverUrl(properties.getPrismaUrl())
        .okHttpClient(okHttpClient)
        .build();
  }
}
