package com.strawberry.app.core.context.person.event;

import com.strawberry.app.common.annotation.SupportedEvents;
import com.strawberry.app.common.event.business.BusinessEvent;
import com.strawberry.app.common.event.business.BusinessEventStream;
import com.strawberry.app.core.context.person.identities.StrawberryPersonId;
import com.strawberry.app.core.context.person.properties.HasStrawberryPersonId;

@SupportedEvents({
    StrawberryPersonAddedEvent.class,
    StrawberryPersonFailedEvent.class
})
public interface StrawberryPersonEvent extends BusinessEvent<StrawberryPersonId>, HasStrawberryPersonId {

  static BusinessEventStream<StrawberryPersonId, StrawberryPersonEvent> eventStream() {
    return new BusinessEventStream<>() {
      @Override
      public Class<StrawberryPersonId> getKeyClass() {
        return StrawberryPersonId.class;
      }

      @Override
      public Class<StrawberryPersonEvent> getValueClass() {
        return StrawberryPersonEvent.class;
      }
    };
  }
}
