package com.strawberry.app.core.context.workday.utils;

import static com.strawberry.app.common.utils.read.event.Util.getModifiedAt;
import static com.strawberry.app.common.utils.read.event.Util.getModifiedBy;

import com.strawberry.app.core.context.workday.command.StrawberryWorkDayCommand;
import com.strawberry.app.core.context.workday.event.StrawberryWorkDayEvent;
import com.strawberry.app.core.context.workday.event.StrawberryWorkDayFailedEvent;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrawberryWorkDayEventBuilderUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(StrawberryWorkDayEventBuilderUtils.class);

  public static StrawberryWorkDayEvent buildFailedEvent(StrawberryWorkDayCommand command, List<String> messages) {
    StrawberryWorkDayEvent event = StrawberryWorkDayFailedEvent.builder()
        .from(command)
        .messages(messages)
        .modifiedAt(getModifiedAt(command))
        .modifiedBy(getModifiedBy(command))
        .build();
    LOGGER.info("{}(identity={}) failed. Produce failed event: {}", command.getClass().getSimpleName(), command.identity().value(), event);
    return event;
  }
}
