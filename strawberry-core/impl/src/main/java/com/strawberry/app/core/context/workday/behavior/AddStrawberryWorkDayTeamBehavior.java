package com.strawberry.app.core.context.workday.behavior;

import static com.strawberry.app.common.ValidationUtils.immutablePopulate;

import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.core.context.workday.StrawberryWorkDay;
import com.strawberry.app.core.context.workday.command.StrawberryWorkDayCommand;
import com.strawberry.app.core.context.workday.event.StrawberryWorkDayEvent;
import com.strawberry.app.core.context.workday.event.StrawberryWorkDayTeamAddedEvent;
import com.strawberry.app.core.context.workday.identities.StrawberryWorkDayId;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddStrawberryWorkDayTeamBehavior implements Behavior<StrawberryWorkDayId, StrawberryWorkDayEvent, StrawberryWorkDayCommand, StrawberryWorkDay> {

  @Override
  public Collection<StrawberryWorkDayEvent> commandToEvents(StrawberryWorkDayCommand command, Optional<StrawberryWorkDay> state) {
    return Collections.emptyList();
  }

  @Override
  public StrawberryWorkDay eventToState(StrawberryWorkDayEvent event, Optional<StrawberryWorkDay> current) {
    StrawberryWorkDayTeamAddedEvent workDayTeamAddedEvent = (StrawberryWorkDayTeamAddedEvent) event;
    return StrawberryWorkDay.builder()
        .from(current.get())
        .from(event)
        .teamIds(immutablePopulate(current.get().teamIds(), workDayTeamAddedEvent.teamId()))
        .modifiedAt(workDayTeamAddedEvent.modifiedAt())
        .modifiedBy(workDayTeamAddedEvent.modifiedBy())
        .build();
  }

  @Override
  public Collection<Class<? extends StrawberryWorkDayCommand>> getSupportedCommands() {
    return Collections.emptyList();
  }

  @Override
  public Collection<Class<? extends StrawberryWorkDayEvent>> getSupportedEvents() {
    return Collections.singletonList(StrawberryWorkDayTeamAddedEvent.class);
  }

  @Override
  public Class<? extends StrawberryWorkDay> getSupportedState() {
    return null;
  }
}
