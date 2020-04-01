package com.strawberry.app.core.context.workday.behavior;

import com.strawberry.app.common.ValidationHelper;
import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.core.context.workday.StrawberryWorkDay;
import com.strawberry.app.core.context.workday.command.AmendStrawberryWorkDayCommand;
import com.strawberry.app.core.context.workday.command.StrawberryWorkDayCommand;
import com.strawberry.app.core.context.workday.event.StrawberryWorkDayAmendedEvent;
import com.strawberry.app.core.context.workday.event.StrawberryWorkDayEvent;
import com.strawberry.app.core.context.workday.identities.StrawberryWorkDayId;
import com.strawberry.app.core.context.workday.properties.HasStrawberryWorkDayId;
import com.strawberry.app.core.context.workday.utils.StrawberryWorkDayEventBuilderUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AmendStrawberryWorkDayBehavior implements
    Behavior<StrawberryWorkDayId, StrawberryWorkDayEvent, StrawberryWorkDayCommand, StrawberryWorkDay> {

  @Override
  public Collection<StrawberryWorkDayEvent> commandToEvents(StrawberryWorkDayCommand command, Optional<StrawberryWorkDay> state) {
    return new ValidationHelper<StrawberryWorkDayId, StrawberryWorkDay, StrawberryWorkDayEvent, AmendStrawberryWorkDayCommand>(state,
        (AmendStrawberryWorkDayCommand) command)
        .present(StrawberryWorkDay.class)
        .success(((amendStrawberryWorkDayCommand, strawberryWorkDay) ->
            StrawberryWorkDayAmendedEvent.builder()
                .from((HasStrawberryWorkDayId) amendStrawberryWorkDayCommand)
                .build()))
        .failed(StrawberryWorkDayEventBuilderUtils::buildFailedEvent);
  }

  @Override
  public StrawberryWorkDay eventToState(StrawberryWorkDayEvent event, Optional<StrawberryWorkDay> current) {
    StrawberryWorkDayAmendedEvent workDayAmendedEvent = (StrawberryWorkDayAmendedEvent) event;
    return StrawberryWorkDay.builder()
        .from(current.get())
        .from(event)
        .modifiedAt(workDayAmendedEvent.modifiedAt())
        .modifiedBy(workDayAmendedEvent.modifiedBy())
        .build();
  }

  @Override
  public Collection<Class<? extends StrawberryWorkDayCommand>> getSupportedCommands() {
    return Collections.singletonList(AmendStrawberryWorkDayCommand.class);
  }

  @Override
  public Collection<Class<? extends StrawberryWorkDayEvent>> getSupportedEvents() {
    return Collections.singletonList(StrawberryWorkDayAmendedEvent.class);
  }

  @Override
  public Class<? extends StrawberryWorkDay> getSupportedState() {
    return StrawberryWorkDay.class;
  }
}
