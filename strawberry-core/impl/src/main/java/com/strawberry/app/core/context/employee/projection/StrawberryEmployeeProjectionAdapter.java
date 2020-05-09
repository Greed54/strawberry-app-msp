package com.strawberry.app.core.context.employee.projection;

import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.event.business.BusinessEventStream;
import com.strawberry.app.common.utils.read.event.FailedEvent;
import com.strawberry.app.common.viewprojector.internal.InternalAbstractProjectionAdapter;
import com.strawberry.app.core.context.employee.StrawberryEmployee;
import com.strawberry.app.core.context.employee.command.StrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeEvent;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.service.StrawberryEmployeeService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrawberryEmployeeProjectionAdapter implements InternalAbstractProjectionAdapter<StrawberryEmployeeId, StrawberryEmployeeEvent> {

  StrawberryEmployeeService employeeService;
  DefaultBehaviorEngine defaultBehaviorEngine;

  @Override
  public void convert(StrawberryEmployeeEvent event) {
    Behavior<StrawberryEmployeeId, StrawberryEmployeeEvent, StrawberryEmployeeCommand, StrawberryEmployee> behavior = defaultBehaviorEngine
        .getBehavior(event.getClass());
    if (!(event instanceof FailedEvent)) {
      defaultBehaviorEngine.project(behavior.eventToState(event, employeeService.getEmployee(event.identity())));
    }
  }

  @Override
  public BusinessEventStream<StrawberryEmployeeId, StrawberryEmployeeEvent> eventStream() {
    return StrawberryEmployeeEvent.eventStream();
  }

}
