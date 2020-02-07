package com.strawberry.app.core.context.team.behavior;

import com.strawberry.app.common.ValidationHelper;
import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.core.context.team.StrawberryTeam;
import com.strawberry.app.core.context.team.command.AmendStrawberryTeamCommand;
import com.strawberry.app.core.context.team.command.StrawberryTeamCommand;
import com.strawberry.app.core.context.team.event.StrawberryTeamAmendedEvent;
import com.strawberry.app.core.context.team.event.StrawberryTeamEvent;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.team.properties.HasStrawberryTeamId;
import com.strawberry.app.core.context.team.utils.StrawberryTeamEventBuilderUtils;
import com.strawberry.app.core.context.team.utils.StrawberryTeamValidator;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class AmendStrawberryTeamBehavior implements
    Behavior<StrawberryTeamId, StrawberryTeamEvent, StrawberryTeamCommand, StrawberryTeam> {

  StrawberryTeamValidator validator;

  @Override
  public Collection<StrawberryTeamEvent> commandToEvents(StrawberryTeamCommand command, Optional<StrawberryTeam> state) {
    return new ValidationHelper<StrawberryTeamId, StrawberryTeam, StrawberryTeamEvent, AmendStrawberryTeamCommand>(state,
        (AmendStrawberryTeamCommand) command)
        .present(StrawberryTeam.class)
        .validateIf(strawberryTeamCommand -> Objects.nonNull(strawberryTeamCommand.teamLeadId()),
            (strawberryTeamCommand, strawberryTeam) -> validator.validateEmployee(strawberryTeamCommand.teamLeadId()))
        .success((strawberryTeamCommand, strawberryTeam) ->
            StrawberryTeamAmendedEvent.builder()
                .from((HasStrawberryTeamId) strawberryTeamCommand)
                .build())
        .failed(StrawberryTeamEventBuilderUtils::buildFailedEvent);
  }

  @Override
  public StrawberryTeam eventToState(StrawberryTeamEvent event, Optional<StrawberryTeam> current) {
    StrawberryTeamAmendedEvent teamAmendedEvent = (StrawberryTeamAmendedEvent) event;
    return StrawberryTeam.builder()
        .from(current.get())
        .from(event)
        .modifiedAt(teamAmendedEvent.modifiedAt())
        .modifiedBy(teamAmendedEvent.modifiedBy())
        .build();
  }

  @Override
  public Collection<Class<? extends StrawberryTeamCommand>> getSupportedCommands() {
    return Collections.singletonList(AmendStrawberryTeamCommand.class);
  }

  @Override
  public Collection<Class<? extends StrawberryTeamEvent>> getSupportedEvents() {
    return Collections.singletonList(StrawberryTeamAmendedEvent.class);
  }

  @Override
  public Class<? extends StrawberryTeam> getSupportedState() {
    return StrawberryTeam.class;
  }
}
