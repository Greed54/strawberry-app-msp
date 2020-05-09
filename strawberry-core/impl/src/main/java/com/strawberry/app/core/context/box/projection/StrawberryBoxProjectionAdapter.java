package com.strawberry.app.core.context.box.projection;

import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.event.business.BusinessEventStream;
import com.strawberry.app.common.utils.read.event.FailedEvent;
import com.strawberry.app.common.viewprojector.internal.InternalAbstractProjectionAdapter;
import com.strawberry.app.core.context.box.StrawberryBox;
import com.strawberry.app.core.context.box.command.StrawberryBoxCommand;
import com.strawberry.app.core.context.box.event.StrawberryBoxEvent;
import com.strawberry.app.core.context.box.identities.StrawberryBoxId;
import com.strawberry.app.core.context.box.service.StrawberryBoxService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrawberryBoxProjectionAdapter implements InternalAbstractProjectionAdapter<StrawberryBoxId, StrawberryBoxEvent> {

  StrawberryBoxService boxService;
  DefaultBehaviorEngine defaultBehaviorEngine;

  @Override
  public void convert(StrawberryBoxEvent event) {
    Behavior<StrawberryBoxId, StrawberryBoxEvent, StrawberryBoxCommand, StrawberryBox> behavior = defaultBehaviorEngine
        .getBehavior(event.getClass());
    if (!(event instanceof FailedEvent)) {
      defaultBehaviorEngine.project(behavior.eventToState(event, boxService.getBox(event.identity())));
    }
  }

  @Override
  public BusinessEventStream<StrawberryBoxId, StrawberryBoxEvent> eventStream() {
    return StrawberryBoxEvent.eventStream();
  }
}
