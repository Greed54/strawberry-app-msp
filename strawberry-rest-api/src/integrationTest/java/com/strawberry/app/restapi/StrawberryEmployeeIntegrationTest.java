package com.strawberry.app.restapi;

import com.strawberry.app.core.context.employee.command.AddStrawberryEmployeeCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StrawberryRestApiApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(TestConfig.class)
@ActiveProfiles("test")
public class StrawberryEmployeeIntegrationTest extends BaseStrawberryRestApiTest {

  @LocalServerPort
  private int port;


  @Test
  public void testAddEmployee() {
    AddStrawberryEmployeeCommand command = RANDOM.nextObject(AddStrawberryEmployeeCommand.class);

    WebTestClient.bindToServer()
        .baseUrl("http://localhost:" + port + "/api/addEmployee")
        .build()
        .post()
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(command)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(String.class);
  }
}
