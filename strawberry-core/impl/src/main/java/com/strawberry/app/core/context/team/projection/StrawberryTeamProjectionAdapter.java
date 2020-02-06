package com.strawberry.app.core.context.team.projection;

import com.strawberry.app.core.context.common.behavior.Behavior;
import com.strawberry.app.core.context.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.core.context.team.StrawberryTeam;
import com.strawberry.app.core.context.team.command.StrawberryTeamCommand;
import com.strawberry.app.core.context.team.event.StrawberryTeamEvent;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.team.service.StrawberryTeamService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StrawberryTeamProjectionAdapter {

  DefaultBehaviorEngine defaultBehaviorEngine;
  StrawberryTeamService teamService;

  Logger LOGGER = LoggerFactory.getLogger(StrawberryTeamProjectionAdapter.class);
  boolean isStarted = false;

  public StrawberryTeamProjectionAdapter(DefaultBehaviorEngine defaultBehaviorEngine,
      StrawberryTeamService teamService) {
    this.defaultBehaviorEngine = defaultBehaviorEngine;
    this.teamService = teamService;
  }

  @EventListener
  public void handleContextStarted(ContextStartedEvent contextStartedEvent) {
    isStarted = true;
  }

  @EventHandler
  public void convert(StrawberryTeamEvent event) {
    if (!isStarted) {
      Behavior<StrawberryTeamId, StrawberryTeamEvent, StrawberryTeamCommand, StrawberryTeam> behavior = defaultBehaviorEngine
          .getBehavior(event.getClass());
      defaultBehaviorEngine.project(behavior.eventToState(event, teamService.getStrawberryTeam(event.identity())));

      LOGGER.info("Restored {}(identity={}), value: {}", event.getClass().getSimpleName(), event.identity(), event);
    }
  }
}
