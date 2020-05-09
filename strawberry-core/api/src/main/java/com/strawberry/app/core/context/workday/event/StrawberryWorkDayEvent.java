package com.strawberry.app.core.context.workday.event;

import com.strawberry.app.common.annotation.SupportedEvents;
import com.strawberry.app.common.event.business.BusinessEvent;
import com.strawberry.app.common.event.business.BusinessEventStream;
import com.strawberry.app.core.context.workday.identities.StrawberryWorkDayId;
import com.strawberry.app.core.context.workday.properties.HasStrawberryWorkDayId;

@SupportedEvents({
    StrawberryWorkDayAmendedEvent.class,
    StrawberryWorkDayInitiatedEvent.class,
    StrawberryWorkDayTeamAddedEvent.class,
    StrawberryWorkDayFailedEvent.class
})
public interface StrawberryWorkDayEvent extends BusinessEvent<StrawberryWorkDayId>, HasStrawberryWorkDayId {

  static BusinessEventStream<StrawberryWorkDayId, StrawberryWorkDayEvent> eventStream() {
    return new BusinessEventStream<>() {
      @Override
      public Class<StrawberryWorkDayId> getKeyClass() {
        return StrawberryWorkDayId.class;
      }

      @Override
      public Class<StrawberryWorkDayEvent> getValueClass() {
        return StrawberryWorkDayEvent.class;
      }
    };
  }
}
