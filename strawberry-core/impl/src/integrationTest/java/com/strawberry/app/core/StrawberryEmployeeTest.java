package com.strawberry.app.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.axonframework.test.matchers.Matchers.andNoMore;
import static org.axonframework.test.matchers.Matchers.exactSequenceOf;
import static org.axonframework.test.matchers.Matchers.predicate;

import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.cqengine.RepositoryFactory;
import com.strawberry.app.common.cqengine.indexedstore.IndexedStoreImpl;
import com.strawberry.app.core.application.store.InternalStoreBuilder;
import com.strawberry.app.core.context.employee.StrawberryEmployee;
import com.strawberry.app.core.context.employee.aggregate.StrawberryEmployeeAggregate;
import com.strawberry.app.core.context.employee.behavior.AddStrawberryEmployeeBehavior;
import com.strawberry.app.core.context.employee.behavior.AmendStrawberryEmployeeBehavior;
import com.strawberry.app.core.context.employee.behavior.AmendStrawberryEmployeeNoteBehavior;
import com.strawberry.app.core.context.employee.behavior.AmendStrawberryEmployeeRoleBehavior;
import com.strawberry.app.core.context.employee.command.AddStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeNoteCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeRoleCommand;
import com.strawberry.app.core.context.employee.command.StrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAddedEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAmendedEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAmendedNoteEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAmendedRoleEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeFailedEvent;
import com.strawberry.app.core.context.employee.projection.StrawberryEmployeeProjectionEvent;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeId;
import com.strawberry.app.core.context.employee.service.StrawberryEmployeeService;
import com.strawberry.app.core.context.employee.utils.StrawberryEmployeeValidator;
import com.strawberry.app.core.context.enums.EmployeeRole;
import com.strawberry.app.core.context.team.StrawberryTeam;
import com.strawberry.app.core.context.team.aggregate.StrawberryTeamAggregate;
import com.strawberry.app.core.context.team.service.StrawberryTeamService;
import com.strawberry.app.core.context.utils.service.RepositoryService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
public class StrawberryEmployeeTest extends BaseStrawberryCoreTest {

  private FixtureConfiguration<StrawberryEmployeeAggregate> fixture;
  private DefaultBehaviorEngine defaultBehaviorEngine;
  private List<IndexedStoreImpl> indexedStores;

  @Before
  public void setUp() {
    indexedStores = InternalStoreBuilder
        .buildStateStores(List.of(new StrawberryEmployeeAggregate(), new StrawberryTeamAggregate()))
        .stream()
        .peek(indexedStore -> indexedStore.init(stateDir, objectMapper))
        .collect(Collectors.toList());
    RepositoryFactory repositoryFactory = new RepositoryFactory(indexedStores);
    RepositoryService repositoryService = new RepositoryService(repositoryFactory);
    StrawberryEmployeeService strawberryEmployeeService = new StrawberryEmployeeService(repositoryService);
    StrawberryEmployeeValidator employeeValidator = new StrawberryEmployeeValidator(new StrawberryTeamService(repositoryService),
        strawberryEmployeeService);
    defaultBehaviorEngine = new DefaultBehaviorEngine(repositoryFactory,
        List.of(
            new AddStrawberryEmployeeBehavior(employeeValidator),
            new AmendStrawberryEmployeeBehavior(employeeValidator),
            new AmendStrawberryEmployeeNoteBehavior(),
            new AmendStrawberryEmployeeRoleBehavior(employeeValidator)));

    fixture = new AggregateTestFixture<>(StrawberryEmployeeAggregate.class)
        .registerInjectableResource(strawberryEmployeeService)
        .registerInjectableResource(defaultBehaviorEngine);
  }

  @Test
  public void addEmployee() {
    StrawberryTeam strawberryTeam = RANDOM.nextObject(StrawberryTeam.class);
    defaultBehaviorEngine.project(strawberryTeam);

    AddStrawberryEmployeeCommand command = RANDOM.nextObject(AddStrawberryEmployeeCommand.class)
        .withTeamId(strawberryTeam.identity());
    StrawberryEmployeeAddedEvent event = StrawberryEmployeeAddedEvent.builder()
        .from((StrawberryEmployeeCommand) command)
        .build();
    StrawberryEmployee state = StrawberryEmployee.builder()
        .from((HasStrawberryEmployeeId) event)
        .build();
    StrawberryEmployeeProjectionEvent projectionEvent = StrawberryEmployeeProjectionEvent.builder()
        .from((HasStrawberryEmployeeId) state)
        .createdBy(state.createdBy())
        .build();

    fixture.givenNoPriorActivity()
        .when(command)
        .expectSuccessfulHandlerExecution()
        .expectEvents(event, projectionEvent)
        .expectState(strawberryEmployeeAggregate -> {
          assertThat(strawberryEmployeeAggregate.getIdentity())
              .isEqualTo(state.identity());

          assertThat(strawberryEmployeeAggregate.getEmployee())
              .isEqualTo(state);
        });
  }

  @Test
  public void addEmployeeWhenTeamNotExist() {
    AddStrawberryEmployeeCommand command = RANDOM.nextObject(AddStrawberryEmployeeCommand.class);

    fixture.givenNoPriorActivity()
        .when(command)
        .expectSuccessfulHandlerExecution()
        .expectEventsMatching(exactSequenceOf(
            predicate(eventMessage -> eventMessage.getPayload() instanceof StrawberryEmployeeFailedEvent),
            andNoMore()));
  }

  @Test
  public void amendEmployee() {
    StrawberryTeam strawberryTeam = RANDOM.nextObject(StrawberryTeam.class);
    defaultBehaviorEngine.project(strawberryTeam);

    StrawberryEmployeeAddedEvent employeeAddedEvent = RANDOM.nextObject(StrawberryEmployeeAddedEvent.class)
        .withTeamId(strawberryTeam.identity());
    StrawberryEmployee initialState = StrawberryEmployee.builder()
        .from((HasStrawberryEmployeeId) employeeAddedEvent)
        .build();
    defaultBehaviorEngine.project(initialState);

    AmendStrawberryEmployeeCommand command = RANDOM.nextObject(AmendStrawberryEmployeeCommand.class)
        .withIdentity(employeeAddedEvent.identity())
        .withTeamId(strawberryTeam.identity());
    StrawberryEmployeeAmendedEvent event = StrawberryEmployeeAmendedEvent.builder()
        .from((HasStrawberryEmployeeId) command)
        .build();
    StrawberryEmployee state = StrawberryEmployee.builder()
        .from(initialState)
        .from((HasStrawberryEmployeeId) event)
        .modifiedAt(event.modifiedAt())
        .modifiedBy(event.modifiedBy())
        .build();
    StrawberryEmployeeProjectionEvent projectionEvent = StrawberryEmployeeProjectionEvent.builder()
        .from((HasStrawberryEmployeeId) state)
        .build();

    fixture.given(employeeAddedEvent)
        .when(command)
        .expectEvents(event, projectionEvent)
        .expectState(strawberryEmployeeAggregate -> {
          assertThat(strawberryEmployeeAggregate.getIdentity())
              .isEqualTo(state.identity());

          assertThat(strawberryEmployeeAggregate.getEmployee())
              .isEqualToIgnoringGivenFields(state, "initShim");
        });
  }

  @Test
  public void amendEmployeeRole() {
    StrawberryEmployeeAddedEvent employeeAddedEvent = RANDOM.nextObject(StrawberryEmployeeAddedEvent.class);
    StrawberryEmployee initialState = StrawberryEmployee.builder()
        .from((HasStrawberryEmployeeId) employeeAddedEvent)
        .build();
    defaultBehaviorEngine.project(initialState);

    AmendStrawberryEmployeeRoleCommand command = RANDOM.nextObject(AmendStrawberryEmployeeRoleCommand.class)
        .withIdentity(employeeAddedEvent.identity())
        .withEmployeeRole(EmployeeRole.TEAM_LEAD);
    StrawberryEmployeeAmendedRoleEvent event = StrawberryEmployeeAmendedRoleEvent.builder()
        .from((HasStrawberryEmployeeId) command)
        .build();
    StrawberryEmployee state = StrawberryEmployee.builder()
        .from(initialState)
        .from((HasStrawberryEmployeeId) event)
        .modifiedAt(event.modifiedAt())
        .modifiedBy(event.modifiedBy())
        .build();
    StrawberryEmployeeProjectionEvent projectionEvent = StrawberryEmployeeProjectionEvent.builder()
        .from((HasStrawberryEmployeeId) state)
        .build();

    fixture.given(employeeAddedEvent)
        .when(command)
        .expectEvents(event, projectionEvent)
        .expectState(strawberryEmployeeAggregate -> {
          assertThat(strawberryEmployeeAggregate.getIdentity())
              .isEqualTo(state.identity());

          assertThat(strawberryEmployeeAggregate.getEmployee())
              .isEqualToIgnoringGivenFields(state, "initShim");
        });
  }

  @Test
  public void amendEmployeeNote() {
    StrawberryEmployeeAddedEvent employeeAddedEvent = RANDOM.nextObject(StrawberryEmployeeAddedEvent.class);
    StrawberryEmployee initialState = StrawberryEmployee.builder()
        .from((HasStrawberryEmployeeId) employeeAddedEvent)
        .build();
    defaultBehaviorEngine.project(initialState);

    AmendStrawberryEmployeeNoteCommand command = RANDOM.nextObject(AmendStrawberryEmployeeNoteCommand.class)
        .withIdentity(employeeAddedEvent.identity());
    StrawberryEmployeeAmendedNoteEvent event = StrawberryEmployeeAmendedNoteEvent.builder()
        .from((HasStrawberryEmployeeId) command)
        .build();
    StrawberryEmployee state = StrawberryEmployee.builder()
        .from(initialState)
        .from((HasStrawberryEmployeeId) event)
        .modifiedAt(event.modifiedAt())
        .modifiedBy(event.modifiedBy())
        .build();
    StrawberryEmployeeProjectionEvent projectionEvent = StrawberryEmployeeProjectionEvent.builder()
        .from((HasStrawberryEmployeeId) state)
        .build();

    fixture.given(employeeAddedEvent)
        .when(command)
        .expectEvents(event, projectionEvent)
        .expectState(strawberryEmployeeAggregate -> {
          assertThat(strawberryEmployeeAggregate.getIdentity())
              .isEqualTo(state.identity());

          assertThat(strawberryEmployeeAggregate.getEmployee())
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
