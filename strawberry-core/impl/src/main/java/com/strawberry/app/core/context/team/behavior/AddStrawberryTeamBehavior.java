package com.strawberry.app.core.context.team.behavior;

import com.strawberry.app.common.ValidationHelper;
import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.core.context.team.StrawberryTeam;
import com.strawberry.app.core.context.team.command.AddStrawberryTeamCommand;
import com.strawberry.app.core.context.team.command.StrawberryTeamCommand;
import com.strawberry.app.core.context.team.event.StrawberryTeamAddedEvent;
import com.strawberry.app.core.context.team.event.StrawberryTeamEvent;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.team.properties.HasStrawberryTeamId;
import com.strawberry.app.core.context.team.utils.StrawberryTeamEventBuilderUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddStrawberryTeamBehavior implements
    Behavior<StrawberryTeamId, StrawberryTeamEvent, StrawberryTeamCommand, StrawberryTeam> {

  @Override
  public Collection<StrawberryTeamEvent> commandToEvents(StrawberryTeamCommand command, Optional<StrawberryTeam> state) {
    return new ValidationHelper<StrawberryTeamId, StrawberryTeam, StrawberryTeamEvent, AddStrawberryTeamCommand>(state,
        (AddStrawberryTeamCommand) command)
        .notPresent()
        .success((addStrawberryTeamCommand, strawberryTeam) ->
            StrawberryTeamAddedEvent.builder()
                .from((HasStrawberryTeamId) addStrawberryTeamCommand)
                .build())
        .failed(StrawberryTeamEventBuilderUtils::buildFailedEvent);

  }

  @Override
  public StrawberryTeam eventToState(StrawberryTeamEvent event, Optional<StrawberryTeam> current) {
    return StrawberryTeam.builder()
        .from(event)
        .createdBy(((StrawberryTeamAddedEvent) event).createdBy())
        .build();
  }

  @Override
  public Collection<Class<? extends StrawberryTeamCommand>> getSupportedCommands() {
    return Collections.singletonList(AddStrawberryTeamCommand.class);
  }

  @Override
  public Collection<Class<? extends StrawberryTeamEvent>> getSupportedEvents() {
    return Collections.singletonList(StrawberryTeamAddedEvent.class);
  }

  @Override
  public Class<? extends StrawberryTeam> getSupportedState() {
    return StrawberryTeam.class;
  }
}
