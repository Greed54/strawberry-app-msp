package com.strawberry.app.read;

import com.apollographql.apollo.ApolloClient;
import com.strawberry.app.common.cqengine.RepositoryFactory;
import com.strawberry.app.common.cqengine.indexedstore.IndexedStoreImpl;
import com.strawberry.app.core.context.employee.projection.StrawberryEmployeeProjectionEvent;
import com.strawberry.app.read.apollo.PrismaClient;
import com.strawberry.app.read.application.store.ExternalProjectionStoreBuilder;
import com.strawberry.app.read.context.StrawberryEmployeeTopology;
import com.strawberry.app.read.context.utils.PrismaMutationResolver;
import com.strawberry.app.read.context.utils.RepositoryService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RunWith(JUnit4.class)
public class StrawberryEmployeeTopologyTest extends BaseStrawberryReadTest {

  StrawberryEmployeeTopology employeeTopology;
  MockWebServer server;
  List<IndexedStoreImpl> indexedStores;

  @Before
  public void setUp() throws IOException {
    server = new MockWebServer();
    server.start();
    final String prismaUrl = "http://" + server.getHostName() + ":" + server.getPort();

    indexedStores = List.of(ExternalProjectionStoreBuilder.buildStrawberryEmployeeProjectionStore())
        .stream()
        .peek(indexedStore -> indexedStore.init(stateDir, objectMapper))
        .collect(Collectors.toList());
    RepositoryService repositoryService = new RepositoryService(new RepositoryFactory(indexedStores));

    OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
    ApolloClient apolloClient = ApolloClient.builder()
        .serverUrl(prismaUrl)
        .okHttpClient(okHttpClient)
        .build();

    employeeTopology = new StrawberryEmployeeTopology(new PrismaClient(apolloClient), new PrismaMutationResolver(repositoryService));
  }

  @Test
  public void addEmployeeTest() {
    server.enqueue(new MockResponse().setBody("done"));
    StrawberryEmployeeProjectionEvent employeeProjectionEvent = RANDOM.nextObject(StrawberryEmployeeProjectionEvent.class);

    employeeTopology.on(employeeProjectionEvent);
  }

  @Test
  public void addExistedEmployeeTest() {
    server.enqueue(new MockResponse().setBody("done"));
    StrawberryEmployeeProjectionEvent employeeProjectionEvent = RANDOM.nextObject(StrawberryEmployeeProjectionEvent.class);

    employeeTopology.on(employeeProjectionEvent);

    server.enqueue(new MockResponse().setBody("done"));
    StrawberryEmployeeProjectionEvent employeeProjectionEvent2 = RANDOM.nextObject(StrawberryEmployeeProjectionEvent.class)
        .withIdentity(employeeProjectionEvent.identity());

    employeeTopology.on(employeeProjectionEvent2);
  }

  @After
  public void tearDown() throws IOException {
    server.shutdown();
    indexedStores.forEach(IndexedStoreImpl::destroy);
    Files.walk(Path.of(stateDir))
        .sorted(Comparator.reverseOrder())
        .map(Path::toFile)
        .forEach(File::deleteOnExit);
  }

}
