package com.strawberry.app.core.context.person.behavior;

import com.strawberry.app.common.ValidationHelper;
import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.core.context.person.StrawberryPerson;
import com.strawberry.app.core.context.person.command.AddStrawberryPersonCommand;
import com.strawberry.app.core.context.person.command.StrawberryPersonCommand;
import com.strawberry.app.core.context.person.event.StrawberryPersonAddedEvent;
import com.strawberry.app.core.context.person.event.StrawberryPersonEvent;
import com.strawberry.app.core.context.person.identities.StrawberryPersonId;
import com.strawberry.app.core.context.person.utils.StrawberryPersonEventBuilderUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddStrawberryPersonBehavior implements Behavior<StrawberryPersonId, StrawberryPersonEvent, StrawberryPersonCommand, StrawberryPerson> {

  @Override
  public Collection<StrawberryPersonEvent> commandToEvents(StrawberryPersonCommand command, Optional<StrawberryPerson> state) {
    return new ValidationHelper<StrawberryPersonId, StrawberryPerson, StrawberryPersonEvent, StrawberryPersonCommand>(state, command)
        .notPresent()
        .success((addStrawberryPersonCommand, strawberryPerson) ->
            StrawberryPersonAddedEvent.builder()
                .from(addStrawberryPersonCommand)
                .build())
        .failed(StrawberryPersonEventBuilderUtils::buildFailedEvent);
  }

  @Override
  public StrawberryPerson eventToState(StrawberryPersonEvent event, Optional<StrawberryPerson> current) {
    return StrawberryPerson.builder()
        .from(event)
        .build();
  }

  @Override
  public Collection<Class<? extends StrawberryPersonCommand>> getSupportedCommands() {
    return Collections.singletonList(AddStrawberryPersonCommand.class);
  }

  @Override
  public Collection<Class<? extends StrawberryPersonEvent>> getSupportedEvents() {
    return Collections.singletonList(StrawberryPersonAddedEvent.class);
  }

  @Override
  public Class<? extends StrawberryPerson> getSupportedState() {
    return StrawberryPerson.class;
  }
}
