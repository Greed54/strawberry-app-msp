package com.strawberry.app.core.context.box.projection;

import static com.strawberry.app.common.MspApplicationProcessorConfiguration.PROJECTION_PROCESSOR_GROUP_NAME;

import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.utils.read.event.FailedEvent;
import com.strawberry.app.core.context.box.StrawberryBox;
import com.strawberry.app.core.context.box.command.StrawberryBoxCommand;
import com.strawberry.app.core.context.box.event.StrawberryBoxEvent;
import com.strawberry.app.core.context.box.identities.StrawberryBoxId;
import com.strawberry.app.core.context.box.service.StrawberryBoxService;
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
public class StrawberryBoxProjectionAdapter {

  StrawberryBoxService boxService;
  DefaultBehaviorEngine defaultBehaviorEngine;

  @EventHandler
  public void convert(StrawberryBoxEvent event) {
    Behavior<StrawberryBoxId, StrawberryBoxEvent, StrawberryBoxCommand, StrawberryBox> behavior = defaultBehaviorEngine
        .getBehavior(event.getClass());
    if (!(event instanceof FailedEvent)) {
      defaultBehaviorEngine.project(behavior.eventToState(event, boxService.getBox(event.identity())));
    }

    log.info("Restored {}(identity={}), value: {}", event.getClass().getSimpleName(), event.identity(), event);
  }
}
