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
import com.strawberry.app.core.context.employee.service.StrawberryEmployeeService;
import com.strawberry.app.core.context.team.StrawberryTeam;
import com.strawberry.app.core.context.team.aggregate.StrawberryTeamAggregate;
import com.strawberry.app.core.context.team.behavior.AddStrawberryTeamBehavior;
import com.strawberry.app.core.context.team.behavior.AmendStrawberryTeamBehavior;
import com.strawberry.app.core.context.team.command.AddStrawberryTeamCommand;
import com.strawberry.app.core.context.team.command.AmendStrawberryTeamCommand;
import com.strawberry.app.core.context.team.command.StrawberryTeamCommand;
import com.strawberry.app.core.context.team.event.StrawberryTeamAddedEvent;
import com.strawberry.app.core.context.team.event.StrawberryTeamAmendedEvent;
import com.strawberry.app.core.context.team.event.StrawberryTeamFailedEvent;
import com.strawberry.app.core.context.team.projection.StrawberryTeamProjectionEvent;
import com.strawberry.app.core.context.team.properties.HasStrawberryTeamId;
import com.strawberry.app.core.context.team.service.StrawberryTeamService;
import com.strawberry.app.core.context.team.utils.StrawberryTeamValidator;
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
public class StrawberryTeamTest extends BaseStrawberryCoreTest {

  private FixtureConfiguration<StrawberryTeamAggregate> fixture;
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
    StrawberryTeamService strawberryTeamService = new StrawberryTeamService(repositoryService);
    StrawberryTeamValidator teamValidator = new StrawberryTeamValidator(strawberryTeamService, new StrawberryEmployeeService(repositoryService));
    defaultBehaviorEngine = new DefaultBehaviorEngine(repositoryFactory,
        List.of(
            new AddStrawberryTeamBehavior(),
            new AmendStrawberryTeamBehavior(teamValidator)));

    fixture = new AggregateTestFixture<>(StrawberryTeamAggregate.class)
        .registerInjectableResource(strawberryTeamService)
        .registerInjectableResource(defaultBehaviorEngine);
  }

  @Test
  public void addTeam() {
    AddStrawberryTeamCommand command = RANDOM.nextObject(AddStrawberryTeamCommand.class);
    StrawberryTeamAddedEvent event = StrawberryTeamAddedEvent.builder()
        .from((StrawberryTeamCommand) command)
        .build();
    StrawberryTeam state = StrawberryTeam.builder()
        .from((HasStrawberryTeamId) event)
        .build();
    StrawberryTeamProjectionEvent projectionEvent = StrawberryTeamProjectionEvent.builder()
        .from((HasStrawberryTeamId) state)
        .createdBy(state.createdBy())
        .build();

    fixture.givenNoPriorActivity()
        .when(command)
        .expectSuccessfulHandlerExecution()
        .expectEvents(event, projectionEvent)
        .expectState(strawberryTeamAggregate -> {
          assertThat(strawberryTeamAggregate.getIdentity())
              .isEqualTo(state.identity());

          assertThat(strawberryTeamAggregate.getTeam())
              .isEqualTo(state);
        });
  }

  @Test
  public void addTeamWhenTeamIsExist() {
    StrawberryTeam strawberryTeam = RANDOM.nextObject(StrawberryTeam.class);
    defaultBehaviorEngine.project(strawberryTeam);

    AddStrawberryTeamCommand command = RANDOM.nextObject(AddStrawberryTeamCommand.class)
        .withIdentity(strawberryTeam.identity());

    fixture.givenNoPriorActivity()
        .when(command)
        .expectSuccessfulHandlerExecution()
        .expectEventsMatching(exactSequenceOf(
            predicate(eventMessage -> eventMessage.getPayload() instanceof StrawberryTeamFailedEvent),
            andNoMore()));
  }

  @Test
  public void amendTeamWhenTeamLeadIdNull() {
    StrawberryTeamAddedEvent teamAddedEvent = RANDOM.nextObject(StrawberryTeamAddedEvent.class);
    StrawberryTeam initialState = StrawberryTeam.builder()
        .from((HasStrawberryTeamId) teamAddedEvent)
        .build();
    defaultBehaviorEngine.project(initialState);

    AmendStrawberryTeamCommand command = RANDOM.nextObject(AmendStrawberryTeamCommand.class)
        .withIdentity(teamAddedEvent.identity())
        .withTeamLeadId(null);
    StrawberryTeamAmendedEvent event = StrawberryTeamAmendedEvent.builder()
        .from((HasStrawberryTeamId) command)
        .build();
    StrawberryTeam state = StrawberryTeam.builder()
        .from(initialState)
        .from((HasStrawberryTeamId) event)
        .modifiedAt(event.modifiedAt())
        .modifiedBy(event.modifiedBy())
        .build();
    StrawberryTeamProjectionEvent projectionEvent = StrawberryTeamProjectionEvent.builder()
        .from((HasStrawberryTeamId) state)
        .build();

    fixture.given(teamAddedEvent)
        .when(command)
        .expectEvents(event, projectionEvent)
        .expectState(strawberryTeamAggregate -> {
          assertThat(strawberryTeamAggregate.getIdentity())
              .isEqualTo(state.identity());

          assertThat(strawberryTeamAggregate.getTeam())
              .isEqualToIgnoringGivenFields(state, "initShim");
        });
  }

  @Test
  public void amendTeamWhenTeamLeadIsExist() {
    StrawberryEmployee strawberryEmployee = RANDOM.nextObject(StrawberryEmployee.class);
    defaultBehaviorEngine.project(strawberryEmployee);

    StrawberryTeamAddedEvent teamAddedEvent = RANDOM.nextObject(StrawberryTeamAddedEvent.class);
    StrawberryTeam initialState = StrawberryTeam.builder()
        .from((HasStrawberryTeamId) teamAddedEvent)
        .build();
    defaultBehaviorEngine.project(initialState);

    AmendStrawberryTeamCommand command = RANDOM.nextObject(AmendStrawberryTeamCommand.class)
        .withIdentity(teamAddedEvent.identity())
        .withTeamLeadId(strawberryEmployee.identity());
    StrawberryTeamAmendedEvent event = StrawberryTeamAmendedEvent.builder()
        .from((HasStrawberryTeamId) command)
        .build();
    StrawberryTeam state = StrawberryTeam.builder()
        .from(initialState)
        .from((HasStrawberryTeamId) event)
        .modifiedAt(event.modifiedAt())
        .modifiedBy(event.modifiedBy())
        .build();
    StrawberryTeamProjectionEvent projectionEvent = StrawberryTeamProjectionEvent.builder()
        .from((HasStrawberryTeamId) state)
        .build();

    fixture.given(teamAddedEvent)
        .when(command)
        .expectEvents(event, projectionEvent)
        .expectState(strawberryTeamAggregate -> {
          assertThat(strawberryTeamAggregate.getIdentity())
              .isEqualTo(state.identity());

          assertThat(strawberryTeamAggregate.getTeam())
              .isEqualToIgnoringGivenFields(state, "initShim");
        });
  }

  @Test
  public void amendTeamWhenTeamLeadIsNotExist() {
    StrawberryEmployee strawberryEmployee = RANDOM.nextObject(StrawberryEmployee.class);

    StrawberryTeamAddedEvent teamAddedEvent = RANDOM.nextObject(StrawberryTeamAddedEvent.class);
    StrawberryTeam initialState = StrawberryTeam.builder()
        .from((HasStrawberryTeamId) teamAddedEvent)
        .build();
    defaultBehaviorEngine.project(initialState);

    AmendStrawberryTeamCommand command = RANDOM.nextObject(AmendStrawberryTeamCommand.class)
        .withIdentity(teamAddedEvent.identity())
        .withTeamLeadId(strawberryEmployee.identity());

    fixture.given(teamAddedEvent)
        .when(command)
        .expectSuccessfulHandlerExecution()
        .expectEventsMatching(exactSequenceOf(
            predicate(eventMessage -> eventMessage.getPayload() instanceof StrawberryTeamFailedEvent),
            andNoMore()));
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
