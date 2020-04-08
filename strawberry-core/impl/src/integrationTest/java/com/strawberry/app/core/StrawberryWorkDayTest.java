package com.strawberry.app.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.cqengine.RepositoryFactory;
import com.strawberry.app.common.cqengine.indexedstore.IndexedStoreImpl;
import com.strawberry.app.common.property.context.identity.PersonId;
import com.strawberry.app.core.application.store.InternalStoreBuilder;
import com.strawberry.app.core.context.box.event.StrawberryBoxAddedEvent;
import com.strawberry.app.core.context.employee.StrawberryEmployee;
import com.strawberry.app.core.context.employee.aggregate.StrawberryEmployeeAggregate;
import com.strawberry.app.core.context.employee.service.StrawberryEmployeeService;
import com.strawberry.app.core.context.team.StrawberryTeam;
import com.strawberry.app.core.context.team.aggregate.StrawberryTeamAggregate;
import com.strawberry.app.core.context.team.service.StrawberryTeamService;
import com.strawberry.app.core.context.utils.service.RepositoryService;
import com.strawberry.app.core.context.workday.StrawberryWorkDay;
import com.strawberry.app.core.context.workday.aggregate.StrawberryWorkDayAggregate;
import com.strawberry.app.core.context.workday.behavior.AddStrawberryWorkDayTeamBehavior;
import com.strawberry.app.core.context.workday.behavior.AmendStrawberryWorkDayBehavior;
import com.strawberry.app.core.context.workday.behavior.InitiateStrawberryWorkDayBehavior;
import com.strawberry.app.core.context.workday.command.AmendStrawberryWorkDayCommand;
import com.strawberry.app.core.context.workday.event.StrawberryBoxToWorkDayAssociationEventHandler;
import com.strawberry.app.core.context.workday.event.StrawberryWorkDayAmendedEvent;
import com.strawberry.app.core.context.workday.event.StrawberryWorkDayInitiatedEvent;
import com.strawberry.app.core.context.workday.event.StrawberryWorkDayTeamAddedEvent;
import com.strawberry.app.core.context.workday.projection.StrawberryWorkDayProjectionEvent;
import com.strawberry.app.core.context.workday.properties.HasStrawberryWorkDayId;
import com.strawberry.app.core.context.workday.service.StrawberryWorkDayService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.axonframework.eventhandling.DomainEventMessage;
import org.axonframework.eventhandling.GenericDomainEventMessage;
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
public class StrawberryWorkDayTest extends BaseStrawberryCoreTest {

  private FixtureConfiguration<StrawberryWorkDayAggregate> fixture;
  private DefaultBehaviorEngine defaultBehaviorEngine;
  private List<IndexedStoreImpl> indexedStores;
  private StrawberryBoxToWorkDayAssociationEventHandler eventHandler;

  @Before
  public void setUp() {
    indexedStores = InternalStoreBuilder
        .buildStateStores(List.of(new StrawberryEmployeeAggregate(), new StrawberryTeamAggregate(), new StrawberryWorkDayAggregate()))
        .stream()
        .peek(indexedStore -> indexedStore.init(stateDir, objectMapper))
        .collect(Collectors.toList());
    RepositoryFactory repositoryFactory = new RepositoryFactory(indexedStores);
    RepositoryService repositoryService = new RepositoryService(repositoryFactory);
    StrawberryWorkDayService workDayService = new StrawberryWorkDayService(repositoryService);
    defaultBehaviorEngine = new DefaultBehaviorEngine(repositoryFactory,
        List.of(
            new InitiateStrawberryWorkDayBehavior(),
            new AmendStrawberryWorkDayBehavior(),
            new AddStrawberryWorkDayTeamBehavior()));

    fixture = new AggregateTestFixture<>(StrawberryWorkDayAggregate.class)
        .registerInjectableResource(workDayService)
        .registerInjectableResource(defaultBehaviorEngine);

    eventHandler = new StrawberryBoxToWorkDayAssociationEventHandler(workDayService, new StrawberryTeamService(repositoryService),
        new StrawberryEmployeeService(repositoryService), null);
  }

//  @Test
//  public void initiateStrawberryWorkDay() {
//    eventHandler.handleEx(RANDOM.nextObject(CardId.class));
//
//    fixture.givenNoPriorActivity()
//        .when(StrawberryWorkDayInitiatedEvent.class)
//        .expectState(strawberryWorkDayAggregate -> {
//          System.out.println(strawberryWorkDayAggregate);
//        });
//  }

  @Test
  public void amendWorkDayTest() {
    StrawberryWorkDayInitiatedEvent workDayInitiatedEvent = RANDOM.nextObject(StrawberryWorkDayInitiatedEvent.class);
    StrawberryWorkDay initialState = StrawberryWorkDay.builder()
        .from((HasStrawberryWorkDayId) workDayInitiatedEvent)
        .build();
    defaultBehaviorEngine.project(initialState);

    AmendStrawberryWorkDayCommand command = RANDOM.nextObject(AmendStrawberryWorkDayCommand.class)
        .withIdentity(workDayInitiatedEvent.identity());
    StrawberryWorkDayAmendedEvent event = StrawberryWorkDayAmendedEvent.builder()
        .from((HasStrawberryWorkDayId) command)
        .build();
    StrawberryWorkDay state = StrawberryWorkDay.builder()
        .from(initialState)
        .from((HasStrawberryWorkDayId) event)
        .modifiedAt(event.modifiedAt())
        .modifiedBy(event.modifiedBy())
        .build();
    StrawberryWorkDayProjectionEvent projectionEvent = StrawberryWorkDayProjectionEvent.builder()
        .from((HasStrawberryWorkDayId) state)
        .build();

    fixture.given(workDayInitiatedEvent)
        .when(command)
        .expectEvents(event, projectionEvent)
        .expectState(strawberryWorkDayAggregate -> {
          assertThat(strawberryWorkDayAggregate.getIdentity())
              .isEqualTo(state.identity());

          assertThat(strawberryWorkDayAggregate.getWorkDay())
              .isEqualToIgnoringGivenFields(state, "initShim");
        });
  }

  @Test
  public void addTeamToWorkDayTest() {
    StrawberryTeam strawberryTeam = RANDOM.nextObject(StrawberryTeam.class);
    defaultBehaviorEngine.project(strawberryTeam);
    StrawberryEmployee strawberryEmployee = RANDOM.nextObject(StrawberryEmployee.class)
        .withTeamId(strawberryTeam.identity());
    defaultBehaviorEngine.project(strawberryEmployee);

    StrawberryWorkDayInitiatedEvent workDayInitiatedEvent = RANDOM.nextObject(StrawberryWorkDayInitiatedEvent.class)
        .withDate(Instant.now().truncatedTo(ChronoUnit.DAYS))
        .withTeamIds(new HashSet<>());
    StrawberryWorkDay initialState = StrawberryWorkDay.builder()
        .from((HasStrawberryWorkDayId) workDayInitiatedEvent)
        .build();
    defaultBehaviorEngine.project(initialState);

    StrawberryBoxAddedEvent boxAddedEvent = RANDOM.nextObject(StrawberryBoxAddedEvent.class)
        .withEmployeeId(strawberryEmployee.identity());

    StrawberryWorkDayTeamAddedEvent event = StrawberryWorkDayTeamAddedEvent.builder()
        .identity(initialState.identity())
        .teamId(strawberryTeam.identity())
        .modifiedAt(Instant.now()) //TODO: ModifiedBy
        .modifiedBy(RANDOM.nextObject(PersonId.class))
        .build();

    GenericDomainEventMessage<StrawberryWorkDayInitiatedEvent> eventMessage = new GenericDomainEventMessage<>(
        StrawberryWorkDayAggregate.class.getSimpleName(), workDayInitiatedEvent.identity().toString(), 1, workDayInitiatedEvent);

    fixture.given(eventMessage);
    eventHandler.handleEx(boxAddedEvent);
    List<? extends DomainEventMessage<?>> collect = fixture.getEventStore().readEvents(workDayInitiatedEvent.identity().toString()).asStream()
        .collect(Collectors.toList());

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
