package com.strawberry.app.core.context.workday.projection;

import static com.strawberry.app.common.MspApplicationProcessorConfiguration.PROJECTION_PROCESSOR_GROUP_NAME;

import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.utils.read.event.FailedEvent;
import com.strawberry.app.core.context.workday.StrawberryWorkDay;
import com.strawberry.app.core.context.workday.command.StrawberryWorkDayCommand;
import com.strawberry.app.core.context.workday.event.StrawberryWorkDayEvent;
import com.strawberry.app.core.context.workday.identities.StrawberryWorkDayId;
import com.strawberry.app.core.context.workday.service.StrawberryWorkDayService;
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
public class StrawberryWorkDayProjectionAdapter {

  StrawberryWorkDayService workDayService;
  DefaultBehaviorEngine defaultBehaviorEngine;

  @EventHandler
  public void convert(StrawberryWorkDayEvent event) {
    Behavior<StrawberryWorkDayId, StrawberryWorkDayEvent, StrawberryWorkDayCommand, StrawberryWorkDay> behavior = defaultBehaviorEngine
        .getBehavior(event.getClass());
    if (!(event instanceof FailedEvent)) {
      defaultBehaviorEngine.project(behavior.eventToState(event, workDayService.getWorkDay(event.identity())));
    }

    log.info("Restored {}(identity={}), value: {}", event.getClass().getSimpleName(), event.identity(), event);
  }
}
