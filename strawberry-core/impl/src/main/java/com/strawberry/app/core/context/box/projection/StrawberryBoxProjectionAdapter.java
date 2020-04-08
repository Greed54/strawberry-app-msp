package com.strawberry.app.core.context.box.projection;

import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.core.context.box.StrawberryBox;
import com.strawberry.app.core.context.box.command.StrawberryBoxCommand;
import com.strawberry.app.core.context.box.event.StrawberryBoxEvent;
import com.strawberry.app.core.context.box.identities.StrawberryBoxId;
import com.strawberry.app.core.context.box.service.StrawberryBoxService;
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
public class StrawberryBoxProjectionAdapter {

  StrawberryBoxService boxService;
  DefaultBehaviorEngine defaultBehaviorEngine;

  Logger LOGGER = LoggerFactory.getLogger(StrawberryBoxProjectionAdapter.class);
  boolean isStarted = false;

  public StrawberryBoxProjectionAdapter(StrawberryBoxService boxService, DefaultBehaviorEngine defaultBehaviorEngine) {
    this.boxService = boxService;
    this.defaultBehaviorEngine = defaultBehaviorEngine;
  }

  @EventListener
  public void handleContextStarted(ContextStartedEvent contextStartedEvent) {
    isStarted = true;
  }

  @EventHandler
  public void convert(StrawberryBoxEvent event) {
    if (!isStarted) {
      Behavior<StrawberryBoxId, StrawberryBoxEvent, StrawberryBoxCommand, StrawberryBox> behavior = defaultBehaviorEngine
          .getBehavior(event.getClass());
      defaultBehaviorEngine.project(behavior.eventToState(event, boxService.getBox(event.identity())));

      LOGGER.info("Restored {}(identity={}), value: {}", event.getClass().getSimpleName(), event.identity(), event);
    }
  }
}
