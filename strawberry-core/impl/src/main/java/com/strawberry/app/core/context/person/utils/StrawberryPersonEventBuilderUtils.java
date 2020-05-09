package com.strawberry.app.core.context.person.utils;

import static com.strawberry.app.common.utils.read.event.Util.getModifiedAt;
import static com.strawberry.app.common.utils.read.event.Util.getModifiedBy;

import com.strawberry.app.core.context.person.command.StrawberryPersonCommand;
import com.strawberry.app.core.context.person.event.StrawberryPersonEvent;
import com.strawberry.app.core.context.person.event.StrawberryPersonFailedEvent;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StrawberryPersonEventBuilderUtils {

  public static StrawberryPersonEvent buildFailedEvent(StrawberryPersonCommand command, List<String> messages) {
    StrawberryPersonEvent event = StrawberryPersonFailedEvent.builder()
        .from(command)
        .messages(messages)
        .modifiedAt(getModifiedAt(command))
        .modifiedBy(getModifiedBy(command))
        .build();
    log.info("{}(identity={}) failed. Produce failed event: {}", command.getClass().getSimpleName(), command.identity().value(), event);
    return event;
  }
}
