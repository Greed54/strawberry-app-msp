package com.strawberry.app.core.context.team.utils;

import static com.strawberry.app.common.utils.read.event.Util.getModifiedAt;
import static com.strawberry.app.common.utils.read.event.Util.getModifiedBy;

import com.strawberry.app.core.context.team.command.StrawberryTeamCommand;
import com.strawberry.app.core.context.team.event.StrawberryTeamEvent;
import com.strawberry.app.core.context.team.event.StrawberryTeamFailedEvent;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrawberryTeamEventBuilderUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(StrawberryTeamEventBuilderUtils.class);

  public static StrawberryTeamEvent buildFailedEvent(StrawberryTeamCommand command, List<String> messages) {
    StrawberryTeamEvent event = StrawberryTeamFailedEvent.builder()
        .from(command)
        .messages(messages)
        .modifiedAt(getModifiedAt(command))
        .modifiedBy(getModifiedBy(command))
        .build();
    LOGGER.info("{}(identity={}) failed. Produce failed event: {}", command.getClass().getSimpleName(), command.identity().value(), event);
    return event;
  }
}
