package com.strawberry.app.core.context.employee.projection;

import static com.strawberry.app.common.MspApplicationProcessorConfiguration.PROJECTION_PROCESSOR_GROUP_NAME;

import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.utils.read.event.FailedEvent;
import com.strawberry.app.core.context.employee.StrawberryEmployee;
import com.strawberry.app.core.context.employee.command.StrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeEvent;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.service.StrawberryEmployeeService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ProcessingGroup(PROJECTION_PROCESSOR_GROUP_NAME)
public class StrawberryEmployeeProjectionAdapter {

  StrawberryEmployeeService employeeService;
  DefaultBehaviorEngine defaultBehaviorEngine;

  @EventHandler
  public void convert(StrawberryEmployeeEvent event) {
    Behavior<StrawberryEmployeeId, StrawberryEmployeeEvent, StrawberryEmployeeCommand, StrawberryEmployee> behavior = defaultBehaviorEngine
        .getBehavior(event.getClass());
    if (!(event instanceof FailedEvent)) {
      defaultBehaviorEngine.project(behavior.eventToState(event, employeeService.getEmployee(event.identity())));
    }

    log.info("Restored {}(identity={}), value: {}", event.getClass().getSimpleName(), event.identity(), event);
  }

}
