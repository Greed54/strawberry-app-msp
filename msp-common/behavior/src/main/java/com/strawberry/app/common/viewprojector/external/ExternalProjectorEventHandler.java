package com.strawberry.app.common.viewprojector.external;

import static com.strawberry.app.common.ProcessorGroupNames.EXTERNAL_PROJECTION_PROCESSOR_GROUP_NAME;

import com.strawberry.app.common.projection.ProjectionEvent;
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
@ProcessingGroup(EXTERNAL_PROJECTION_PROCESSOR_GROUP_NAME)
public class ExternalProjectorEventHandler {

  ExternalProjectionAdapterEngine externalProjectionAdapterEngine;

  @EventHandler
  public void convert(ProjectionEvent projectionEvent) {
    ExternalAbstractProjectionAdapter adapter = externalProjectionAdapterEngine.getAdapter(projectionEvent.getClass());
    if (adapter != null) {
      adapter.convert(projectionEvent);
      log.info("Restored {}(identity={}), value: {}", projectionEvent.getClass().getSimpleName(), projectionEvent.identity(), projectionEvent);
    }
  }

}
