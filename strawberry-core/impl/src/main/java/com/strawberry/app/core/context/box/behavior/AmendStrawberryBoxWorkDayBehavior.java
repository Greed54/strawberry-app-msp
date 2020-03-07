package com.strawberry.app.core.context.box.behavior;

import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.core.context.box.StrawberryBox;
import com.strawberry.app.core.context.box.command.StrawberryBoxCommand;
import com.strawberry.app.core.context.box.event.StrawberryBoxEvent;
import com.strawberry.app.core.context.box.event.StrawberryBoxWorkDayAmendedEvent;
import com.strawberry.app.core.context.box.identities.StrawberryBoxId;
import com.strawberry.app.core.context.box.properties.HasStrawberryBoxId;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class AmendStrawberryBoxWorkDayBehavior implements Behavior<StrawberryBoxId, StrawberryBoxEvent, StrawberryBoxCommand, StrawberryBox> {

  @Override
  public Collection<StrawberryBoxEvent> commandToEvents(StrawberryBoxCommand command, Optional<StrawberryBox> state) {
    return Collections.emptyList();
  }

  @Override
  public StrawberryBox eventToState(StrawberryBoxEvent event, Optional<StrawberryBox> current) {
    StrawberryBoxWorkDayAmendedEvent workDayAmendedEvent = (StrawberryBoxWorkDayAmendedEvent) event;
    return StrawberryBox.builder()
        .from(current.get())
        .from((HasStrawberryBoxId) workDayAmendedEvent)
        .workDayId(workDayAmendedEvent.workDayId())
        .modifiedAt(workDayAmendedEvent.modifiedAt())
        .modifiedBy(workDayAmendedEvent.modifiedBy())
        .build();
  }

  @Override
  public Collection<Class<? extends StrawberryBoxCommand>> getSupportedCommands() {
    return Collections.emptyList();
  }

  @Override
  public Collection<Class<? extends StrawberryBoxEvent>> getSupportedEvents() {
    return Collections.singletonList(StrawberryBoxWorkDayAmendedEvent.class);
  }

  @Override
  public Class<? extends StrawberryBox> getSupportedState() {
    return StrawberryBox.class;
  }
}
