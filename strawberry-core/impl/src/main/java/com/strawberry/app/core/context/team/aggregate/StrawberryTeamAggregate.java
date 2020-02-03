package com.strawberry.app.core.context.team.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import com.strawberry.app.core.context.common.behavior.Behavior;
import com.strawberry.app.core.context.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.core.context.team.StrawberryTeam;
import com.strawberry.app.core.context.team.command.AddStrawberryTeamCommand;
import com.strawberry.app.core.context.team.command.AmendStrawberryTeamCommand;
import com.strawberry.app.core.context.team.command.StrawberryTeamCommand;
import com.strawberry.app.core.context.team.event.StrawberryTeamEvent;
import com.strawberry.app.core.context.team.event.StrawberryTeamFailedEvent;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.team.projection.StrawberryTeamProjectionEvent;
import com.strawberry.app.core.context.team.properties.HasStrawberryTeamId;
import com.strawberry.app.core.context.team.service.StrawberryTeamService;
import java.util.Collection;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StrawberryTeamAggregate {

  @AggregateIdentifier
  StrawberryTeamId identity;

  StrawberryTeam team;

  public StrawberryTeamAggregate() {
  }

  @CommandHandler
  public StrawberryTeamAggregate(AddStrawberryTeamCommand command, DefaultBehaviorEngine defaultBehaviorEngine, StrawberryTeamService teamService) {
    Behavior<StrawberryTeamId, StrawberryTeamEvent, StrawberryTeamCommand, StrawberryTeam> behavior = defaultBehaviorEngine
        .getBehavior(command.getClass());
    Collection<StrawberryTeamEvent> strawberryTeamEvents = behavior
        .commandToEvents(command, teamService.getStrawberryTeam(command.identity()));
    strawberryTeamEvents.forEach(AggregateLifecycle::apply);
  }

  @CommandHandler
  public void handleEx(AmendStrawberryTeamCommand command, DefaultBehaviorEngine defaultBehaviorEngine, StrawberryTeamService teamService) {
    Behavior<StrawberryTeamId, StrawberryTeamEvent, StrawberryTeamCommand, StrawberryTeam> behavior = defaultBehaviorEngine
        .getBehavior(command.getClass());
    behavior.commandToEvents(command, teamService.getStrawberryTeam(command.identity())).forEach(AggregateLifecycle::apply);
  }

  @EventHandler
  public void on(StrawberryTeamEvent event, DefaultBehaviorEngine defaultBehaviorEngine) {
    if (!(event instanceof StrawberryTeamFailedEvent)) {
      Behavior<StrawberryTeamId, StrawberryTeamEvent, StrawberryTeamCommand, StrawberryTeam> behavior = defaultBehaviorEngine
          .getBehavior(event.getClass());
      this.identity = event.identity();
      this.team = behavior.eventToState(event, Optional.ofNullable(team));
      defaultBehaviorEngine.project(team);

      apply(StrawberryTeamProjectionEvent.builder()
          .from((HasStrawberryTeamId) team)
          .build());
    }
  }
}
