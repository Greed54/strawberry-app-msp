package com.strawberry.app.core.context.employee.event;

import com.strawberry.app.common.annotation.SupportedEvents;
import com.strawberry.app.common.event.business.BusinessEvent;
import com.strawberry.app.common.event.business.BusinessEventStream;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeId;

@SupportedEvents({
    StrawberryEmployeeAddedEvent.class,
    StrawberryEmployeeAmendedEvent.class,
    StrawberryEmployeeAmendedNoteEvent.class,
    StrawberryEmployeeAmendedRoleEvent.class,
    StrawberryEmployeeFailedEvent.class
})
public interface StrawberryEmployeeEvent extends BusinessEvent<StrawberryEmployeeId>, HasStrawberryEmployeeId {

  static BusinessEventStream<StrawberryEmployeeId, StrawberryEmployeeEvent> eventStream() {
    return new BusinessEventStream<>() {
      @Override
      public Class<StrawberryEmployeeId> getKeyClass() {
        return StrawberryEmployeeId.class;
      }

      @Override
      public Class<StrawberryEmployeeEvent> getValueClass() {
        return StrawberryEmployeeEvent.class;
      }
    };
  }
}
