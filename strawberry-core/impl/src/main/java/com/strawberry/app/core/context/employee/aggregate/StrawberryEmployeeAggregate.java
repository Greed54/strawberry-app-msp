package com.strawberry.app.core.context.employee.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import com.strawberry.app.core.context.employee.StrawberryEmployee;
import com.strawberry.app.core.context.employee.command.AddStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeNoteCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeRoleCommand;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAddedEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAmendedEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAmendedRoleEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAmendedNoteEvent;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StrawberryEmployeeAggregate {

  @AggregateIdentifier
  @Getter
  StrawberryEmployeeId identity;
  @Getter
  @Setter
  StrawberryEmployee employee;

  @CommandHandler
  public StrawberryEmployeeAggregate(AddStrawberryEmployeeCommand command) {
    apply(StrawberryEmployeeAddedEvent.builder()
        .from((HasStrawberryEmployeeId) command)
        .build());
  }

  @EventSourcingHandler
  public void on(StrawberryEmployeeAddedEvent event) {
    this.identity = event.identity();
    this.employee = StrawberryEmployee.builder()
        .from((HasStrawberryEmployeeId) event)
        .build();
  }

  @CommandHandler
  public void handle(AmendStrawberryEmployeeCommand command) {
    apply(StrawberryEmployeeAmendedEvent.builder()
        .from((HasStrawberryEmployeeId) command)
        .build());
  }

  @EventSourcingHandler
  public void on(StrawberryEmployeeAmendedEvent event) {
    if (event.identity().equals(identity)) {
      this.employee = StrawberryEmployee.builder()
          .from(employee)
          .from((HasStrawberryEmployeeId) event)
          .modifiedAt(event.modifiedAt())
          .modifiedBy(event.modifiedBy())
          .build();
    }
  }

  @CommandHandler
  public void handle(AmendStrawberryEmployeeRoleCommand command) {
    apply(StrawberryEmployeeAmendedRoleEvent.builder()
        .from((HasStrawberryEmployeeId) command)
        .build());
  }

  @EventSourcingHandler
  public void on(StrawberryEmployeeAmendedRoleEvent event) {
    if (event.identity().equals(identity)) {
      this.employee = StrawberryEmployee.builder()
          .from(employee)
          .from((HasStrawberryEmployeeId) event)
          .modifiedAt(event.modifiedAt())
          .modifiedBy(event.modifiedBy())
          .build();
    }
  }

  @CommandHandler
  public void handle(AmendStrawberryEmployeeNoteCommand command) {
    apply(StrawberryEmployeeAmendedNoteEvent.builder()
        .from((HasStrawberryEmployeeId) command)
        .build());
  }

  @EventSourcingHandler
  public void on(StrawberryEmployeeAmendedNoteEvent event) {
    if (event.identity().equals(identity)) {
      this.employee = StrawberryEmployee.builder()
          .from(employee)
          .from((HasStrawberryEmployeeId) event)
          .modifiedAt(event.modifiedAt())
          .modifiedBy(event.modifiedBy())
          .build();
    }
  }

  public StrawberryEmployeeAggregate() {
  }

}
