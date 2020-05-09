package com.strawberry.app.core.context.box.event;

import com.strawberry.app.common.annotation.SupportedEvents;
import com.strawberry.app.common.event.business.BusinessEvent;
import com.strawberry.app.common.event.business.BusinessEventStream;
import com.strawberry.app.core.context.box.identities.StrawberryBoxId;
import com.strawberry.app.core.context.box.properties.HasStrawberryBoxId;

@SupportedEvents({
    StrawberryBoxAddedEvent.class,
    StrawberryBoxWorkDayAmendedEvent.class,
    StrawberryBoxFailedEvent.class
})
public interface StrawberryBoxEvent extends BusinessEvent<StrawberryBoxId>, HasStrawberryBoxId {

  static BusinessEventStream<StrawberryBoxId, StrawberryBoxEvent> eventStream() {
    return new BusinessEventStream<>() {
      @Override
      public Class<StrawberryBoxId> getKeyClass() {
        return StrawberryBoxId.class;
      }

      @Override
      public Class<StrawberryBoxEvent> getValueClass() {
        return StrawberryBoxEvent.class;
      }
    };
  }
}
