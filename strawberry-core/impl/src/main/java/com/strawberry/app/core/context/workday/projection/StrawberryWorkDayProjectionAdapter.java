package com.strawberry.app.core.context.workday.projection;

import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.core.context.workday.StrawberryWorkDay;
import com.strawberry.app.core.context.workday.command.StrawberryWorkDayCommand;
import com.strawberry.app.core.context.workday.event.StrawberryWorkDayEvent;
import com.strawberry.app.core.context.workday.identities.StrawberryWorkDayId;
import com.strawberry.app.core.context.workday.service.StrawberryWorkDayService;
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
public class StrawberryWorkDayProjectionAdapter {

  StrawberryWorkDayService workDayService;
  DefaultBehaviorEngine defaultBehaviorEngine;

  Logger LOGGER = LoggerFactory.getLogger(StrawberryWorkDayProjectionAdapter.class);
  boolean isStarted = false;

  public StrawberryWorkDayProjectionAdapter(StrawberryWorkDayService workDayService,
      DefaultBehaviorEngine defaultBehaviorEngine) {
    this.workDayService = workDayService;
    this.defaultBehaviorEngine = defaultBehaviorEngine;
  }

  @EventListener
  public void handleContextStarted(ContextStartedEvent contextStartedEvent) {
    isStarted = true;
  }

  @EventHandler
  public void convert(StrawberryWorkDayEvent event) {
    if (!isStarted) {
      Behavior<StrawberryWorkDayId, StrawberryWorkDayEvent, StrawberryWorkDayCommand, StrawberryWorkDay> behavior = defaultBehaviorEngine
          .getBehavior(event.getClass());
      defaultBehaviorEngine.project(behavior.eventToState(event, workDayService.getWorkDay(event.identity())));

      LOGGER.info("Restored {}(identity={}), value: {}", event.getClass().getSimpleName(), event.identity(), event);
    }
  }
}
