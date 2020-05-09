package com.strawberry.app.core.context.team.event;

import com.strawberry.app.common.annotation.SupportedEvents;
import com.strawberry.app.common.event.business.BusinessEvent;
import com.strawberry.app.common.event.business.BusinessEventStream;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.team.properties.HasStrawberryTeamId;

@SupportedEvents({
    StrawberryTeamAddedEvent.class,
    StrawberryTeamAmendedEvent.class,
    StrawberryTeamFailedEvent.class
})
public interface StrawberryTeamEvent extends BusinessEvent<StrawberryTeamId>, HasStrawberryTeamId {

  static BusinessEventStream<StrawberryTeamId, StrawberryTeamEvent> eventStream() {
    return new BusinessEventStream<>() {
      @Override
      public Class<StrawberryTeamId> getKeyClass() {
        return StrawberryTeamId.class;
      }

      @Override
      public Class<StrawberryTeamEvent> getValueClass() {
        return StrawberryTeamEvent.class;
      }
    };
  }
}
