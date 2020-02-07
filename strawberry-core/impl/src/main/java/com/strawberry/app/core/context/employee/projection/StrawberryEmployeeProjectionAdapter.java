package com.strawberry.app.core.context.employee.projection;

import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.core.context.employee.StrawberryEmployee;
import com.strawberry.app.core.context.employee.command.StrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeEvent;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.service.StrawberryEmployeeService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StrawberryEmployeeProjectionAdapter {

  StrawberryEmployeeService employeeService;
  DefaultBehaviorEngine defaultBehaviorEngine;

  Logger LOGGER = LoggerFactory.getLogger(StrawberryEmployeeProjectionAdapter.class);
  boolean isStarted = false;

  public StrawberryEmployeeProjectionAdapter(StrawberryEmployeeService employeeService, DefaultBehaviorEngine defaultBehaviorEngine) {
    this.employeeService = employeeService;
    this.defaultBehaviorEngine = defaultBehaviorEngine;
  }

  @EventListener
  public void handleContextStarted(ContextStartedEvent contextStartedEvent) {
    isStarted = true;
  }

  @EventHandler
  public void convert(StrawberryEmployeeEvent event) {
    if (!isStarted) {
      Behavior<StrawberryEmployeeId, StrawberryEmployeeEvent, StrawberryEmployeeCommand, StrawberryEmployee> behavior = defaultBehaviorEngine
          .getBehavior(event.getClass());
      defaultBehaviorEngine.project(behavior.eventToState(event, employeeService.getEmployee(event.identity())));

      LOGGER.info("Restored {}(identity={}), value: {}", event.getClass().getSimpleName(), event.identity(), event);
    }
  }

}
