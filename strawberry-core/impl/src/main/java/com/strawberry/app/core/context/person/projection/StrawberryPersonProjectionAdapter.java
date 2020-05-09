package com.strawberry.app.core.context.person.projection;

import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.event.business.BusinessEventStream;
import com.strawberry.app.common.utils.read.event.FailedEvent;
import com.strawberry.app.common.viewprojector.internal.InternalAbstractProjectionAdapter;
import com.strawberry.app.core.context.person.StrawberryPerson;
import com.strawberry.app.core.context.person.command.StrawberryPersonCommand;
import com.strawberry.app.core.context.person.event.StrawberryPersonEvent;
import com.strawberry.app.core.context.person.identities.StrawberryPersonId;
import com.strawberry.app.core.context.person.service.StrawberryPersonService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrawberryPersonProjectionAdapter implements InternalAbstractProjectionAdapter<StrawberryPersonId, StrawberryPersonEvent> {

  StrawberryPersonService personService;
  DefaultBehaviorEngine defaultBehaviorEngine;

  @Override
  public void convert(StrawberryPersonEvent event) {
    Behavior<StrawberryPersonId, StrawberryPersonEvent, StrawberryPersonCommand, StrawberryPerson> behavior = defaultBehaviorEngine
        .getBehavior(event.getClass());
    if (!(event instanceof FailedEvent)) {
      defaultBehaviorEngine.project(behavior.eventToState(event, personService.getPerson(event.identity())));
    }
  }

  @Override
  public BusinessEventStream<StrawberryPersonId, StrawberryPersonEvent> eventStream() {
    return StrawberryPersonEvent.eventStream();
  }

}
