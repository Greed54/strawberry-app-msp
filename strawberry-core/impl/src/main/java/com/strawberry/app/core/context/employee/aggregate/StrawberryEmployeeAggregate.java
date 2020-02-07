package com.strawberry.app.core.context.employee.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.core.context.employee.StrawberryEmployee;
import com.strawberry.app.core.context.employee.command.AddStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeNoteCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeRoleCommand;
import com.strawberry.app.core.context.employee.command.StrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeFailedEvent;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.projection.StrawberryEmployeeProjectionEvent;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeId;
import com.strawberry.app.core.context.employee.service.StrawberryEmployeeService;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StrawberryEmployeeAggregate {

  @AggregateIdentifier
  StrawberryEmployeeId identity;

  StrawberryEmployee employee;

  public StrawberryEmployeeAggregate() {
  }

  @CommandHandler
  public StrawberryEmployeeAggregate(AddStrawberryEmployeeCommand command, DefaultBehaviorEngine defaultBehaviorEngine,
      StrawberryEmployeeService employeeService) {
    Behavior<StrawberryEmployeeId, StrawberryEmployeeEvent, StrawberryEmployeeCommand, StrawberryEmployee> behavior = defaultBehaviorEngine
        .getBehavior(command.getClass());
    behavior.commandToEvents(command, employeeService.getEmployee(command.identity())).forEach(AggregateLifecycle::apply);
  }

  @CommandHandler
  public void handleEx(AmendStrawberryEmployeeCommand command, DefaultBehaviorEngine defaultBehaviorEngine,
      StrawberryEmployeeService employeeService) {
    Behavior<StrawberryEmployeeId, StrawberryEmployeeEvent, StrawberryEmployeeCommand, StrawberryEmployee> behavior = defaultBehaviorEngine
        .getBehavior(command.getClass());
    behavior.commandToEvents(command, employeeService.getEmployee(command.identity())).forEach(AggregateLifecycle::apply);
  }

  @CommandHandler
  public void handleEx(AmendStrawberryEmployeeRoleCommand command, DefaultBehaviorEngine defaultBehaviorEngine,
      StrawberryEmployeeService employeeService) {
    Behavior<StrawberryEmployeeId, StrawberryEmployeeEvent, StrawberryEmployeeCommand, StrawberryEmployee> behavior = defaultBehaviorEngine
        .getBehavior(command.getClass());
    behavior.commandToEvents(command, employeeService.getEmployee(command.identity())).forEach(AggregateLifecycle::apply);
  }

  @CommandHandler
  public void handleEx(AmendStrawberryEmployeeNoteCommand command, DefaultBehaviorEngine defaultBehaviorEngine,
      StrawberryEmployeeService employeeService) {
    Behavior<StrawberryEmployeeId, StrawberryEmployeeEvent, StrawberryEmployeeCommand, StrawberryEmployee> behavior = defaultBehaviorEngine
        .getBehavior(command.getClass());
    behavior.commandToEvents(command, employeeService.getEmployee(command.identity())).forEach(AggregateLifecycle::apply);
  }

  @EventHandler
  public void addStrawberryEmployee(StrawberryEmployeeEvent event, DefaultBehaviorEngine defaultBehaviorEngine) {
    if (!(event instanceof StrawberryEmployeeFailedEvent)) {
      Behavior<StrawberryEmployeeId, StrawberryEmployeeEvent, StrawberryEmployeeCommand, StrawberryEmployee> behavior = defaultBehaviorEngine
          .getBehavior(event.getClass());
      this.identity = event.identity();
      this.employee = behavior.eventToState(event, Optional.ofNullable(employee));
      defaultBehaviorEngine.project(employee);

      apply(StrawberryEmployeeProjectionEvent.builder()
          .from((HasStrawberryEmployeeId) employee)
          .build());
    }
  }
}
