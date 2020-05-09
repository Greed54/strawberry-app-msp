package com.strawberry.app.core.context.workday.projection;

import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.event.business.BusinessEventStream;
import com.strawberry.app.common.utils.read.event.FailedEvent;
import com.strawberry.app.common.viewprojector.internal.InternalAbstractProjectionAdapter;
import com.strawberry.app.core.context.workday.StrawberryWorkDay;
import com.strawberry.app.core.context.workday.command.StrawberryWorkDayCommand;
import com.strawberry.app.core.context.workday.event.StrawberryWorkDayEvent;
import com.strawberry.app.core.context.workday.identities.StrawberryWorkDayId;
import com.strawberry.app.core.context.workday.service.StrawberryWorkDayService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrawberryWorkDayProjectionAdapter implements InternalAbstractProjectionAdapter<StrawberryWorkDayId, StrawberryWorkDayEvent> {

  StrawberryWorkDayService workDayService;
  DefaultBehaviorEngine defaultBehaviorEngine;

  @Override
  public void convert(StrawberryWorkDayEvent event) {
    Behavior<StrawberryWorkDayId, StrawberryWorkDayEvent, StrawberryWorkDayCommand, StrawberryWorkDay> behavior = defaultBehaviorEngine
        .getBehavior(event.getClass());
    if (!(event instanceof FailedEvent)) {
      defaultBehaviorEngine.project(behavior.eventToState(event, workDayService.getWorkDay(event.identity())));
    }
  }

  @Override
  public BusinessEventStream<StrawberryWorkDayId, StrawberryWorkDayEvent> eventStream() {
    return StrawberryWorkDayEvent.eventStream();
  }
}
