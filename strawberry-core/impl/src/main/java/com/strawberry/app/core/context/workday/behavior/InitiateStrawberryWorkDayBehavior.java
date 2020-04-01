package com.strawberry.app.core.context.workday.behavior;

import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.core.context.workday.StrawberryWorkDay;
import com.strawberry.app.core.context.workday.command.StrawberryWorkDayCommand;
import com.strawberry.app.core.context.workday.event.StrawberryWorkDayEvent;
import com.strawberry.app.core.context.workday.event.StrawberryWorkDayInitiatedEvent;
import com.strawberry.app.core.context.workday.identities.StrawberryWorkDayId;
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
public class InitiateStrawberryWorkDayBehavior implements
    Behavior<StrawberryWorkDayId, StrawberryWorkDayEvent, StrawberryWorkDayCommand, StrawberryWorkDay> {

  @Override
  public Collection<StrawberryWorkDayEvent> commandToEvents(StrawberryWorkDayCommand command, Optional<StrawberryWorkDay> state) {
    return Collections.emptyList();
  }

  @Override
  public StrawberryWorkDay eventToState(StrawberryWorkDayEvent event, Optional<StrawberryWorkDay> current) {
    return StrawberryWorkDay.builder()
        .from(event)
        .build();
  }

  @Override
  public Collection<Class<? extends StrawberryWorkDayCommand>> getSupportedCommands() {
    return Collections.emptyList();
  }

  @Override
  public Collection<Class<? extends StrawberryWorkDayEvent>> getSupportedEvents() {
    return Collections.singletonList(StrawberryWorkDayInitiatedEvent.class);
  }

  @Override
  public Class<? extends StrawberryWorkDay> getSupportedState() {
    return StrawberryWorkDay.class;
  }
}
