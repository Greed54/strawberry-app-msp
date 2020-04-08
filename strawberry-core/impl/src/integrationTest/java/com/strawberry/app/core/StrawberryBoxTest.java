package com.strawberry.app.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.cqengine.RepositoryFactory;
import com.strawberry.app.common.cqengine.indexedstore.IndexedStoreImpl;
import com.strawberry.app.common.property.context.identity.PersonId;
import com.strawberry.app.core.application.store.InternalStoreBuilder;
import com.strawberry.app.core.context.box.StrawberryBox;
import com.strawberry.app.core.context.box.aggregate.StrawberryBoxAggregate;
import com.strawberry.app.core.context.box.behavior.AddStrawberryBoxBehavior;
import com.strawberry.app.core.context.box.behavior.AmendStrawberryBoxWorkDayBehavior;
import com.strawberry.app.core.context.box.command.AddStrawberryBoxCommand;
import com.strawberry.app.core.context.box.event.StrawberryBoxAddedEvent;
import com.strawberry.app.core.context.box.event.StrawberryBoxWorkDayAmendedEvent;
import com.strawberry.app.core.context.box.projecton.StrawberryBoxProjectionEvent;
import com.strawberry.app.core.context.box.properties.HasStrawberryBoxId;
import com.strawberry.app.core.context.box.service.StrawberryBoxService;
import com.strawberry.app.core.context.box.utils.StrawberryBoxValidator;
import com.strawberry.app.core.context.employee.StrawberryEmployee;
import com.strawberry.app.core.context.employee.aggregate.StrawberryEmployeeAggregate;
import com.strawberry.app.core.context.employee.service.StrawberryEmployeeService;
import com.strawberry.app.core.context.team.aggregate.StrawberryTeamAggregate;
import com.strawberry.app.core.context.utils.service.RepositoryService;
import com.strawberry.app.core.context.workday.StrawberryWorkDay;
import com.strawberry.app.core.context.workday.aggregate.StrawberryWorkDayAggregate;
import com.strawberry.app.core.context.workday.service.StrawberryWorkDayService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@Ignore
@RunWith(JUnit4.class)
public class StrawberryBoxTest extends BaseStrawberryCoreTest {

  private FixtureConfiguration<StrawberryBoxAggregate> fixture;
  private DefaultBehaviorEngine defaultBehaviorEngine;
  private List<IndexedStoreImpl> indexedStores;

  @Before
  public void setUp() {
    indexedStores = InternalStoreBuilder
        .buildStateStores(
            List.of(new StrawberryEmployeeAggregate(), new StrawberryTeamAggregate(), new StrawberryWorkDayAggregate(), new StrawberryBoxAggregate()))
        .stream()
        .peek(indexedStore -> indexedStore.init(stateDir, objectMapper))
        .collect(Collectors.toList());
    RepositoryFactory repositoryFactory = new RepositoryFactory(indexedStores);
    RepositoryService repositoryService = new RepositoryService(repositoryFactory);
    StrawberryEmployeeService employeeService = new StrawberryEmployeeService(repositoryService);
    defaultBehaviorEngine = new DefaultBehaviorEngine(repositoryFactory,
        List.of(
            new AddStrawberryBoxBehavior(new StrawberryBoxValidator(employeeService), employeeService),
            new AmendStrawberryBoxWorkDayBehavior()));

    fixture = new AggregateTestFixture<>(StrawberryBoxAggregate.class)
        .registerInjectableResource(new StrawberryBoxService(repositoryService))
        .registerInjectableResource(new StrawberryWorkDayService(repositoryService))
        .registerInjectableResource(defaultBehaviorEngine);

  }

  @Test
  public void addBoxTest() {
    StrawberryWorkDay strawberryWorkDay = RANDOM.nextObject(StrawberryWorkDay.class)
        .withDate(Instant.now().truncatedTo(ChronoUnit.DAYS));
    defaultBehaviorEngine.project(strawberryWorkDay);

    StrawberryEmployee strawberryEmployee = RANDOM.nextObject(StrawberryEmployee.class);
    defaultBehaviorEngine.project(strawberryEmployee);

    AddStrawberryBoxCommand command = RANDOM.nextObject(AddStrawberryBoxCommand.class)
        .withCardId(strawberryEmployee.cardId().value());
    StrawberryBoxAddedEvent addedEvent = StrawberryBoxAddedEvent.builder()
        .from((HasStrawberryBoxId) command)
        .employeeId(strawberryEmployee.identity())
        .build();
    StrawberryBoxWorkDayAmendedEvent amendedEvent = StrawberryBoxWorkDayAmendedEvent.builder()
        .from(command)
        .workDayId(strawberryWorkDay.identity())
        .modifiedAt(command.createdAt())
        .modifiedBy(new PersonId())
        .build();
    StrawberryBox state = StrawberryBox.builder()
        .from((HasStrawberryBoxId) addedEvent)
        .from((HasStrawberryBoxId) amendedEvent)
        .modifiedAt(command.createdAt())
        .modifiedBy(new PersonId())
        .build();
    StrawberryBoxProjectionEvent projectionEvent1 = StrawberryBoxProjectionEvent.builder()
        .from((HasStrawberryBoxId) addedEvent)
        .build();
    StrawberryBoxProjectionEvent projectionEvent2 = StrawberryBoxProjectionEvent.builder()
        .from((HasStrawberryBoxId) state)
        .build();

    fixture.givenNoPriorActivity()
        .when(command)
        .expectEvents(addedEvent, amendedEvent, projectionEvent1, projectionEvent2)
        .expectState(strawberryBoxAggregate -> {
          assertThat(strawberryBoxAggregate.getIdentity())
              .isEqualTo(state.identity());

          assertThat(strawberryBoxAggregate.getBox())
              .isEqualToIgnoringGivenFields(state, "initShim");
        });
  }

  @After
  public void tearDown() throws IOException {
    indexedStores.forEach(IndexedStoreImpl::destroy);
    Files.walk(Path.of(stateDir))
        .sorted(Comparator.reverseOrder())
        .map(Path::toFile)
        .forEach(File::deleteOnExit);
  }
}
