package com.strawberry.app.core.context.employee.aggregate;

import com.strawberry.app.core.context.employee.StrawberryEmployee;
import com.strawberry.app.core.context.employee.behavior.AddStrawberryEmployeeBehavior;
import com.strawberry.app.core.context.employee.behavior.AmendStrawberryEmployeeBehavior;
import com.strawberry.app.core.context.employee.behavior.AmendStrawberryEmployeeNoteBehavior;
import com.strawberry.app.core.context.employee.behavior.AmendStrawberryEmployeeRoleBehavior;
import com.strawberry.app.core.context.employee.command.AddStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeNoteCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeRoleCommand;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAddedEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAmendedEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAmendedNoteEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAmendedRoleEvent;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
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

  public StrawberryEmployeeAggregate() {
  }

  @CommandHandler
  public StrawberryEmployeeAggregate(AddStrawberryEmployeeCommand command, AddStrawberryEmployeeBehavior behavior) {
    behavior.commandToEvents(command, Optional.ofNullable(employee)).forEach(AggregateLifecycle::apply);
  }

  @EventSourcingHandler
  public void addStrawberryEmployee(StrawberryEmployeeAddedEvent event, AddStrawberryEmployeeBehavior behavior) {
    this.identity = event.identity();
    this.employee = behavior.eventToState(event, Optional.ofNullable(employee));
  }

  @CommandHandler
  public void handle(AmendStrawberryEmployeeCommand command, AmendStrawberryEmployeeBehavior behavior) {
    behavior.commandToEvents(command, Optional.ofNullable(employee)).forEach(AggregateLifecycle::apply);
  }

  @EventSourcingHandler
  public void on(StrawberryEmployeeAmendedEvent event, AmendStrawberryEmployeeBehavior behavior) {
    if (event.identity().equals(identity)) {
      this.employee = behavior.eventToState(event, Optional.ofNullable(employee));
    }
  }

  @CommandHandler
  public void handle(AmendStrawberryEmployeeRoleCommand command, AmendStrawberryEmployeeRoleBehavior behavior) {
    behavior.commandToEvents(command, Optional.ofNullable(employee)).forEach(AggregateLifecycle::apply);
  }

  @EventSourcingHandler
  public void on(StrawberryEmployeeAmendedRoleEvent event, AmendStrawberryEmployeeRoleBehavior behavior) {
    if (event.identity().equals(identity)) {
      this.employee = behavior.eventToState(event, Optional.ofNullable(employee));
    }
  }

  @CommandHandler
  public void handle(AmendStrawberryEmployeeNoteCommand command, AmendStrawberryEmployeeNoteBehavior behavior) {
    behavior.commandToEvents(command, Optional.ofNullable(employee)).forEach(AggregateLifecycle::apply);
  }

  @EventSourcingHandler
  public void on(StrawberryEmployeeAmendedNoteEvent event, AmendStrawberryEmployeeNoteBehavior behavior) {
    if (event.identity().equals(identity)) {
      this.employee = behavior.eventToState(event, Optional.ofNullable(employee));
    }
  }
}
