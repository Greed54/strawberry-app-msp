package com.strawberry.app.core.context.utils;

import static com.strawberry.app.common.ProcessorGroupNames.LOGGING_PROCESSOR_GROUP_NAME;

import com.strawberry.app.common.event.business.BusinessEvent;
import com.strawberry.app.common.projection.ProjectionEvent;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ProcessingGroup(LOGGING_PROCESSOR_GROUP_NAME)
public class LogEventHandler {

  @EventHandler
  public void handleBusinessEvent(BusinessEvent event) {
    log.info("Received {} (identity={}), value: {}", event.getClass().getSimpleName(), event.identity(), event);
  }

  @EventHandler
  public void handleProjectionEvent(ProjectionEvent projectionEvent) {
    log.info("Published projection event {} (identity={}), value: {}", projectionEvent.getClass().getSimpleName(), projectionEvent.identity(),
        projectionEvent);
  }
}
