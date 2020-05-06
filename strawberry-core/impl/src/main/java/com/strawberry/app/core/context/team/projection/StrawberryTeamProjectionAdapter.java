package com.strawberry.app.core.context.team.projection;

import static com.strawberry.app.common.MspApplicationProcessorConfiguration.PROJECTION_PROCESSOR_GROUP_NAME;

import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.utils.read.event.FailedEvent;
import com.strawberry.app.core.context.team.StrawberryTeam;
import com.strawberry.app.core.context.team.command.StrawberryTeamCommand;
import com.strawberry.app.core.context.team.event.StrawberryTeamEvent;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.team.service.StrawberryTeamService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ProcessingGroup(PROJECTION_PROCESSOR_GROUP_NAME)
public class StrawberryTeamProjectionAdapter {

  DefaultBehaviorEngine defaultBehaviorEngine;
  StrawberryTeamService teamService;

  @EventHandler
  public void convert(StrawberryTeamEvent event) {
    Behavior<StrawberryTeamId, StrawberryTeamEvent, StrawberryTeamCommand, StrawberryTeam> behavior = defaultBehaviorEngine
        .getBehavior(event.getClass());
    if (!(event instanceof FailedEvent)) {
      defaultBehaviorEngine.project(behavior.eventToState(event, teamService.getStrawberryTeam(event.identity())));
    }

    log.info("Restored {}(identity={}), value: {}", event.getClass().getSimpleName(), event.identity(), event);
  }
}
