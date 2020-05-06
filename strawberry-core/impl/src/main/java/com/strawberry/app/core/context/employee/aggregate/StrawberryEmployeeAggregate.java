package com.strawberry.app.core.context.employee.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import com.google.common.collect.ImmutableSet;
import com.strawberry.app.common.aggregate.AbstractAggregate;
import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.cqengine.ProjectionIndex;
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
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StrawberryEmployeeAggregate implements
    AbstractAggregate<StrawberryEmployeeId, StrawberryEmployeeCommand, StrawberryEmployeeEvent, StrawberryEmployee> {

  @AggregateIdentifier
  @Getter
  StrawberryEmployeeId identity;

  @Getter
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

  @Override
  @EventHandler
  public void handleEvent(StrawberryEmployeeEvent businessEvent, DefaultBehaviorEngine behaviorEngine) {
    if (!(businessEvent instanceof StrawberryEmployeeFailedEvent)) {
      Behavior<StrawberryEmployeeId, StrawberryEmployeeEvent, StrawberryEmployeeCommand, StrawberryEmployee> behavior = behaviorEngine
          .getBehavior(businessEvent.getClass());
      this.identity = businessEvent.identity();
      this.employee = behavior.eventToState(businessEvent, Optional.ofNullable(employee));

      projectState(employee, behaviorEngine);
      publishProjectionEvent(employee);
    } else {
      this.identity = businessEvent.identity();
    }
  }

  @Override
  public Class<StrawberryEmployeeId> identityClass() {
    return StrawberryEmployeeId.class;
  }

  @Override
  public Class<StrawberryEmployee> stateClass() {
    return StrawberryEmployee.class;
  }

  @Override
  public ImmutableSet<ProjectionIndex<StrawberryEmployee>> indices() {
    return StrawberryEmployee.INDICES;
  }

  @Override
  public void publishProjectionEvent(StrawberryEmployee state) {
    apply(StrawberryEmployeeProjectionEvent.builder()
        .from((HasStrawberryEmployeeId) state)
        .build());
  }
}
