package com.strawberry.app.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
import com.strawberry.app.core.context.employee.projection.StrawberryEmployeeProjectionAdapter;
import com.strawberry.app.core.context.employee.projection.StrawberryEmployeeProjectionEvent;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeId;
import com.strawberry.app.core.context.employee.repository.StrawberryEmployeeProjectionRepository;
import com.strawberry.app.core.context.employee.service.StrawberryEmployeeProjectionService;
import com.strawberry.app.core.context.employee.utils.StrawberryEmployeeValidator;
import com.strawberry.app.core.context.enums.EmployeeRole;
import com.strawberry.app.core.context.team.projection.StrawberryTeamProjectionEvent;
import com.strawberry.app.core.context.team.repository.StrawberryTeamProjectionRepository;
import com.strawberry.app.core.context.team.service.StrawberryTeamProjectionService;
import com.strawberry.app.core.context.utils.Util;
import java.util.Optional;
import org.axonframework.eventhandling.DomainEventMessage;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.eventsourcing.eventstore.DomainEventStream;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(TestConfig.class)
public class StrawberryEmployeeTest extends BaseStrawberryCoreTest {

  private FixtureConfiguration<StrawberryEmployeeAggregate> fixture;

  private StrawberryEmployeeProjectionAdapter employeeProjectionAdapter;
  private StrawberryEmployeeProjectionService employeeProjectionService;

  private StrawberryTeamProjectionService teamProjectionService;

  @Autowired
  @Qualifier(value = "eEventStore")
  private EmbeddedEventStore eEventStore;
  @Mock
  private EventGateway eventGateway;

  @Autowired
  private StrawberryEmployeeProjectionRepository employeeProjectionRepository;
  @Autowired
  private StrawberryTeamProjectionRepository teamProjectionRepository;

  @Before
  public void setUp() {
    teamProjectionService = new StrawberryTeamProjectionService(teamProjectionRepository);
    employeeProjectionService = new StrawberryEmployeeProjectionService(employeeProjectionRepository);
    StrawberryEmployeeValidator employeeValidator = new StrawberryEmployeeValidator(teamProjectionService, employeeProjectionService);
    employeeProjectionAdapter = new StrawberryEmployeeProjectionAdapter(employeeProjectionService, eventGateway);
    fixture = new AggregateTestFixture<>(StrawberryEmployeeAggregate.class)
        .registerInjectableResource(new AddStrawberryEmployeeBehavior(employeeValidator))
        .registerInjectableResource(new AmendStrawberryEmployeeBehavior(employeeValidator))
        .registerInjectableResource(new AmendStrawberryEmployeeNoteBehavior())
        .registerInjectableResource(new AmendStrawberryEmployeeRoleBehavior(employeeValidator));
  }

  @Test
  public void addEmployee() {
    StrawberryTeamProjectionEvent teamProjectionEvent = RANDOM.nextObject(StrawberryTeamProjectionEvent.class);
    teamProjectionService.saveProjection(Util.mapTeamProjectionEvent(teamProjectionEvent));

    AddStrawberryEmployeeCommand command = RANDOM.nextObject(AddStrawberryEmployeeCommand.class)
        .withTeamId(teamProjectionEvent.identity());
    StrawberryEmployeeAddedEvent employeeAddedEvent = StrawberryEmployeeAddedEvent.builder()
        .from((StrawberryEmployeeCommand) command)
        .build();

    StrawberryEmployee employee = StrawberryEmployee.builder()
        .from((HasStrawberryEmployeeId) command)
        .build();

    StrawberryEmployeeProjectionEvent employeeProjectionEvent = StrawberryEmployeeProjectionEvent.builder()
        .from((HasStrawberryEmployeeId) command)
        .createdBy(employee.createdBy())
        .build();

    fixture.givenNoPriorActivity()
        .when(command)
        .expectSuccessfulHandlerExecution()
        .expectEvents(employeeAddedEvent);

    employeeProjectionAdapter.onProject(employeeAddedEvent);
    DomainEventMessage<?> next = eEventStore.readEvents(employeeProjectionEvent.identity().value()).next();
  }

  @Test
  public void amendEmployee() {
    StrawberryEmployeeAddedEvent employeeAddedEvent = RANDOM.nextObject(StrawberryEmployeeAddedEvent.class);

    AmendStrawberryEmployeeCommand amendStrawberryEmployeeCommand = RANDOM.nextObject(AmendStrawberryEmployeeCommand.class)
        .withIdentity(employeeAddedEvent.identity());
    StrawberryEmployeeAmendedEvent amendedEvent = StrawberryEmployeeAmendedEvent.builder()
        .from((HasStrawberryEmployeeId) amendStrawberryEmployeeCommand)
        .build();
    StrawberryEmployeeProjectionEvent employeeProjectionEvent = StrawberryEmployeeProjectionEvent.builder()
        .from((HasStrawberryEmployeeId) employeeAddedEvent)
        .from((HasStrawberryEmployeeId) amendStrawberryEmployeeCommand)
        .modifiedAt(amendStrawberryEmployeeCommand.modifiedAt())
        .modifiedBy(amendStrawberryEmployeeCommand.modifiedBy())
        .createdAt(employeeAddedEvent.createdAt())
        .build();

    StrawberryEmployee employee = StrawberryEmployee.builder()
        .from((HasStrawberryEmployeeId) employeeAddedEvent)
        .from((HasStrawberryEmployeeId) amendStrawberryEmployeeCommand)
        .modifiedAt(amendStrawberryEmployeeCommand.modifiedAt())
        .modifiedBy(amendStrawberryEmployeeCommand.modifiedBy())
        .createdAt(employeeAddedEvent.createdAt())
        .build();

    fixture.given(employeeAddedEvent)
        .when(amendStrawberryEmployeeCommand)
        .expectEvents(amendedEvent, employeeProjectionEvent)
        .expectState(strawberryEmployeeAggregate -> {
          assertThat(strawberryEmployeeAggregate.getIdentity())
              .isEqualTo(amendStrawberryEmployeeCommand.identity());

          assertThat(strawberryEmployeeAggregate.getEmployee())
              .isEqualToIgnoringGivenFields(employee, "initShim");
        });
  }

  @Test
  public void amendEmployeeRole() {
    StrawberryEmployeeAddedEvent employeeAddedEvent = RANDOM.nextObject(StrawberryEmployeeAddedEvent.class);

    AmendStrawberryEmployeeRoleCommand amendStrawberryEmployeeRoleCommand = RANDOM.nextObject(AmendStrawberryEmployeeRoleCommand.class)
        .withIdentity(employeeAddedEvent.identity())
        .withEmployeeRole(EmployeeRole.TEAM_LEAD);

    StrawberryEmployeeAmendedRoleEvent strawberryEmployeeAmendedRoleEvent = StrawberryEmployeeAmendedRoleEvent.builder()
        .from((HasStrawberryEmployeeId) amendStrawberryEmployeeRoleCommand)
        .build();
    StrawberryEmployeeProjectionEvent employeeProjectionEvent = StrawberryEmployeeProjectionEvent.builder()
        .from((HasStrawberryEmployeeId) employeeAddedEvent)
        .from((HasStrawberryEmployeeId) amendStrawberryEmployeeRoleCommand)
        .modifiedAt(amendStrawberryEmployeeRoleCommand.modifiedAt())
        .modifiedBy(amendStrawberryEmployeeRoleCommand.modifiedBy())
        .createdAt(employeeAddedEvent.createdAt())
        .build();

    StrawberryEmployee employee = StrawberryEmployee.builder()
        .from((HasStrawberryEmployeeId) employeeAddedEvent)
        .from((HasStrawberryEmployeeId) amendStrawberryEmployeeRoleCommand)
        .modifiedAt(amendStrawberryEmployeeRoleCommand.modifiedAt())
        .modifiedBy(amendStrawberryEmployeeRoleCommand.modifiedBy())
        .createdAt(employeeAddedEvent.createdAt())
        .build();

    fixture.given(employeeAddedEvent)
        .when(amendStrawberryEmployeeRoleCommand)
        .expectEvents(strawberryEmployeeAmendedRoleEvent, employeeProjectionEvent)
        .expectState(strawberryEmployeeAggregate -> {
          assertThat(strawberryEmployeeAggregate.getIdentity())
              .isEqualTo(amendStrawberryEmployeeRoleCommand.identity());

          assertThat(strawberryEmployeeAggregate.getEmployee())
              .isEqualToIgnoringGivenFields(employee, "initShim");
        });
  }

  @Test
  public void amendEmployeeNote() {
    StrawberryEmployeeAddedEvent employeeAddedEvent = RANDOM.nextObject(StrawberryEmployeeAddedEvent.class);

    AmendStrawberryEmployeeNoteCommand amendStrawberryEmployeeNoteCommand = RANDOM.nextObject(AmendStrawberryEmployeeNoteCommand.class)
        .withIdentity(employeeAddedEvent.identity());

    StrawberryEmployeeAmendedNoteEvent strawberryEmployeeAmendedNoteEvent = StrawberryEmployeeAmendedNoteEvent.builder()
        .from((HasStrawberryEmployeeId) amendStrawberryEmployeeNoteCommand)
        .build();
    StrawberryEmployeeProjectionEvent employeeProjectionEvent = StrawberryEmployeeProjectionEvent.builder()
        .from((HasStrawberryEmployeeId) employeeAddedEvent)
        .from((HasStrawberryEmployeeId) strawberryEmployeeAmendedNoteEvent)
        .modifiedAt(strawberryEmployeeAmendedNoteEvent.modifiedAt())
        .modifiedBy(strawberryEmployeeAmendedNoteEvent.modifiedBy())
        .createdAt(employeeAddedEvent.createdAt())
        .build();

    StrawberryEmployee employee = StrawberryEmployee.builder()
        .from((HasStrawberryEmployeeId) employeeAddedEvent)
        .from((HasStrawberryEmployeeId) amendStrawberryEmployeeNoteCommand)
        .modifiedAt(amendStrawberryEmployeeNoteCommand.modifiedAt())
        .modifiedBy(amendStrawberryEmployeeNoteCommand.modifiedBy())
        .createdAt(employeeAddedEvent.createdAt())
        .build();

    fixture.given(employeeAddedEvent)
        .when(amendStrawberryEmployeeNoteCommand)
        .expectEvents(strawberryEmployeeAmendedNoteEvent, employeeProjectionEvent)
        .expectState(strawberryEmployeeAggregate -> {
          assertThat(strawberryEmployeeAggregate.getIdentity())
              .isEqualTo(amendStrawberryEmployeeNoteCommand.identity());

          assertThat(strawberryEmployeeAggregate.getEmployee())
              .isEqualToIgnoringGivenFields(employee, "initShim");
        });
  }
}
