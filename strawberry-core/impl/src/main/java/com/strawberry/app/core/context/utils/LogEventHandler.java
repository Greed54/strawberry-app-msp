package com.strawberry.app.core.context.utils;

import com.strawberry.app.core.context.cqrscommon.event.BusinessEvent;
import com.strawberry.app.core.context.employee.aggregate.StrawberryEmployeeAggregate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LogEventHandler {

  Logger LOGGER = LoggerFactory.getLogger(StrawberryEmployeeAggregate.class);

  @EventHandler
  public void handleBusinessEvent(BusinessEvent event) {
    LOGGER.info("Received {}(identity={}), value: {}", event.getClass().getSimpleName(), event.identity(), event);
  }
}
