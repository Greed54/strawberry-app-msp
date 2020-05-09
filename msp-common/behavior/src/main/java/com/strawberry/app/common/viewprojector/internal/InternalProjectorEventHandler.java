package com.strawberry.app.common.viewprojector.internal;

import static com.strawberry.app.common.ProcessorGroupNames.PROJECTION_PROCESSOR_GROUP_NAME;

import com.strawberry.app.common.event.business.BusinessEvent;
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
public class InternalProjectorEventHandler {

  InternalProjectionAdapterEngine internalProjectionAdapterEngine;

  @EventHandler
  public void convert(BusinessEvent businessEvent) {
    internalProjectionAdapterEngine.getAdapter(businessEvent.getClass()).convert(businessEvent);
    log.info("Restored {}(identity={}), value: {}", businessEvent.getClass().getSimpleName(), businessEvent.identity(), businessEvent);
  }

}
