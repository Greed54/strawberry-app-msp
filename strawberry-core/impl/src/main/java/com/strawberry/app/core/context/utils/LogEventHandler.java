package com.strawberry.app.core.context.utils;

import com.strawberry.app.common.event.BusinessEvent;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LogEventHandler {

  Logger LOGGER = LoggerFactory.getLogger(LogEventHandler.class);

  @EventHandler
  public void handleBusinessEvent(BusinessEvent event) {
    LOGGER.info("Received {}(identity={}), value: {}", event.getClass().getSimpleName(), event.identity(), event);
  }
}
