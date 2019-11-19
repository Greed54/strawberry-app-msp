package com.strawberry.app.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.strawberry.app.core.context.employee.StrawberryEmployee;
import com.strawberry.app.core.context.employee.aggregate.StrawberryEmployeeAggregate;
import com.strawberry.app.core.context.employee.command.AddStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeNoteCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeRoleCommand;
import com.strawberry.app.core.context.employee.command.StrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAddedEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAmendedEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAmendedNoteEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAmendedRoleEvent;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeId;
import com.strawberry.app.core.context.enums.EmployeeRole;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class StrawberryEmployeeTest extends BaseStrawberryCoreTest {

  private FixtureConfiguration<StrawberryEmployeeAggregate> fixture;

  @Before
  public void setUp() {
    fixture = new AggregateTestFixture<>(StrawberryEmployeeAggregate.class);
  }

  @Test
  public void addEmployee() {
    AddStrawberryEmployeeCommand command = RANDOM.nextObject(AddStrawberryEmployeeCommand.class);
    StrawberryEmployeeAddedEvent employeeAddedEvent = StrawberryEmployeeAddedEvent.builder()
        .from((StrawberryEmployeeCommand) command)
        .build();

    StrawberryEmployee employee = StrawberryEmployee.builder()
        .from((HasStrawberryEmployeeId) command)
        .build();

    fixture.givenNoPriorActivity()
        .when(command)
        .expectEvents(employeeAddedEvent)
        .expectState(strawberryEmployeeAggregate -> {
          assertThat(strawberryEmployeeAggregate.getIdentity())
              .isEqualTo(command.identity());

          assertThat(strawberryEmployeeAggregate.getEmployee())
              .isEqualToIgnoringGivenFields(employee, "initShim");
        });
  }

  @Test
  public void amendEmployee() {
    StrawberryEmployeeAddedEvent employeeAddedEvent = RANDOM.nextObject(StrawberryEmployeeAddedEvent.class);

    AmendStrawberryEmployeeCommand amendStrawberryEmployeeCommand = RANDOM.nextObject(AmendStrawberryEmployeeCommand.class)
        .withIdentity(employeeAddedEvent.identity());
    StrawberryEmployeeAmendedEvent amendedEvent = StrawberryEmployeeAmendedEvent.builder()
        .from((HasStrawberryEmployeeId) amendStrawberryEmployeeCommand)
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
        .expectEvents(amendedEvent)
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

    StrawberryEmployee employee = StrawberryEmployee.builder()
        .from((HasStrawberryEmployeeId) employeeAddedEvent)
        .from((HasStrawberryEmployeeId) amendStrawberryEmployeeRoleCommand)
        .modifiedAt(amendStrawberryEmployeeRoleCommand.modifiedAt())
        .modifiedBy(amendStrawberryEmployeeRoleCommand.modifiedBy())
        .createdAt(employeeAddedEvent.createdAt())
        .build();

    fixture.given(employeeAddedEvent)
        .when(amendStrawberryEmployeeRoleCommand)
        .expectEvents(strawberryEmployeeAmendedRoleEvent)
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

    StrawberryEmployee employee = StrawberryEmployee.builder()
        .from((HasStrawberryEmployeeId) employeeAddedEvent)
        .from((HasStrawberryEmployeeId) amendStrawberryEmployeeNoteCommand)
        .modifiedAt(amendStrawberryEmployeeNoteCommand.modifiedAt())
        .modifiedBy(amendStrawberryEmployeeNoteCommand.modifiedBy())
        .createdAt(employeeAddedEvent.createdAt())
        .build();

    fixture.given(employeeAddedEvent)
        .when(amendStrawberryEmployeeNoteCommand)
        .expectEvents(strawberryEmployeeAmendedNoteEvent)
        .expectState(strawberryEmployeeAggregate -> {
          assertThat(strawberryEmployeeAggregate.getIdentity())
              .isEqualTo(amendStrawberryEmployeeNoteCommand.identity());

          assertThat(strawberryEmployeeAggregate.getEmployee())
              .isEqualToIgnoringGivenFields(employee, "initShim");
        });
  }
}
