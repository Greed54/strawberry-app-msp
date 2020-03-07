package com.strawberry.app.core.context.box.utils;

import static com.strawberry.app.common.utils.read.event.Util.getModifiedAt;
import static com.strawberry.app.common.utils.read.event.Util.getModifiedBy;

import com.strawberry.app.core.context.box.command.StrawberryBoxCommand;
import com.strawberry.app.core.context.box.event.StrawberryBoxEvent;
import com.strawberry.app.core.context.box.event.StrawberryBoxFailedEvent;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrawberryBoxEventBuilderUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(StrawberryBoxEventBuilderUtils.class);

  public static StrawberryBoxEvent buildFailedEvent(StrawberryBoxCommand command, List<String> messages) {
    StrawberryBoxEvent event = StrawberryBoxFailedEvent.builder()
        .from(command)
        .messages(messages)
        .modifiedAt(getModifiedAt(command))
        .modifiedBy(getModifiedBy(command))
        .build();
    LOGGER.info("{}(identity={}) failed. Produce failed event: {}", command.getClass().getSimpleName(), command.identity().value(), event);
    return event;
  }
}
