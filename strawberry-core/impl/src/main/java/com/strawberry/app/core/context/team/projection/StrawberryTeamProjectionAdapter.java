package com.strawberry.app.core.context.team.projection;

import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.event.business.BusinessEventStream;
import com.strawberry.app.common.utils.read.event.FailedEvent;
import com.strawberry.app.common.viewprojector.internal.InternalAbstractProjectionAdapter;
import com.strawberry.app.core.context.team.StrawberryTeam;
import com.strawberry.app.core.context.team.command.StrawberryTeamCommand;
import com.strawberry.app.core.context.team.event.StrawberryTeamEvent;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.team.service.StrawberryTeamService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrawberryTeamProjectionAdapter implements InternalAbstractProjectionAdapter<StrawberryTeamId, StrawberryTeamEvent> {

  DefaultBehaviorEngine defaultBehaviorEngine;
  StrawberryTeamService teamService;

  @Override
  public void convert(StrawberryTeamEvent event) {
    Behavior<StrawberryTeamId, StrawberryTeamEvent, StrawberryTeamCommand, StrawberryTeam> behavior = defaultBehaviorEngine
        .getBehavior(event.getClass());
    if (!(event instanceof FailedEvent)) {
      defaultBehaviorEngine.project(behavior.eventToState(event, teamService.getStrawberryTeam(event.identity())));
    }
  }

  @Override
  public BusinessEventStream<StrawberryTeamId, StrawberryTeamEvent> eventStream() {
    return StrawberryTeamEvent.eventStream();
  }
}
