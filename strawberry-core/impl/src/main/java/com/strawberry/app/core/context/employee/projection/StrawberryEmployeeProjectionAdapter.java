package com.strawberry.app.core.context.employee.projection;

import com.strawberry.app.core.context.employee.event.StrawberryEmployeeEvent;
import com.strawberry.app.core.context.employee.service.StrawberryEmployeeProjectionService;
import com.strawberry.app.core.context.utils.Util;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrawberryEmployeeProjectionAdapter {

  StrawberryEmployeeProjectionService employeeProjectionService;
  EventGateway eventGateway;
  Logger LOGGER = LoggerFactory.getLogger(StrawberryEmployeeProjectionAdapter.class);

  public StrawberryEmployeeProjectionAdapter(
      StrawberryEmployeeProjectionService employeeProjectionService, EventGateway eventGateway) {
    this.employeeProjectionService = employeeProjectionService;
    this.eventGateway = eventGateway;
  }

  @EventHandler
  public void onProject(StrawberryEmployeeEvent event) {
    StrawberryEmployeeProjectionEvent employeeProjectionEvent = StrawberryEmployeeProjectionEvent.builder()
        .from(event)
        .build();

    eventGateway.publish(employeeProjectionEvent);

    StrawberryEmployeeProjectionEntity strawberryEmployeeProjectionEntity = employeeProjectionService
        .saveProjection(Util.mapEmployeeProjectionEvent(employeeProjectionEvent));

    LOGGER.info("Projected {}(identity={}), value: {}", event.getClass().getSimpleName(), strawberryEmployeeProjectionEntity.getIdentity(),
        employeeProjectionEvent);
  }
}
