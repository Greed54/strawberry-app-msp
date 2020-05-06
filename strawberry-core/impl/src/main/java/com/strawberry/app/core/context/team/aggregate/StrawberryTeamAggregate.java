package com.strawberry.app.core.context.team.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import com.google.common.collect.ImmutableSet;
import com.strawberry.app.common.aggregate.AbstractAggregate;
import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.cqengine.ProjectionIndex;
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
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@FieldDefaults(level = AccessLevel.PRIVATE)
@ProcessingGroup("StrawberryTeamAggregate")
public class StrawberryTeamAggregate implements AbstractAggregate<StrawberryTeamId, StrawberryTeamCommand, StrawberryTeamEvent, StrawberryTeam> {

  @AggregateIdentifier
  @Getter
  StrawberryTeamId identity;
  @Getter
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

  @Override
  @EventHandler
  public void handleEvent(StrawberryTeamEvent businessEvent, DefaultBehaviorEngine behaviorEngine) {
    if (!(businessEvent instanceof StrawberryTeamFailedEvent)) {
      Behavior<StrawberryTeamId, StrawberryTeamEvent, StrawberryTeamCommand, StrawberryTeam> behavior = behaviorEngine
          .getBehavior(businessEvent.getClass());
      this.identity = businessEvent.identity();
      this.team = behavior.eventToState(businessEvent, Optional.ofNullable(team));

      projectState(team, behaviorEngine);
      publishProjectionEvent(team);
    } else {
      this.identity = businessEvent.identity();
    }
  }

  @Override
  public Class<StrawberryTeamId> identityClass() {
    return StrawberryTeamId.class;
  }

  @Override
  public Class<StrawberryTeam> stateClass() {
    return StrawberryTeam.class;
  }

  @Override
  public ImmutableSet<ProjectionIndex<StrawberryTeam>> indices() {
    return StrawberryTeam.INDICES;
  }

  @Override
  public void publishProjectionEvent(StrawberryTeam state) {
    apply(StrawberryTeamProjectionEvent.builder()
        .from((HasStrawberryTeamId) team)
        .build());
  }
}
