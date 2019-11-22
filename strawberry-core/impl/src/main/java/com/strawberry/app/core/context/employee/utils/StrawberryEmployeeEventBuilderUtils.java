package com.strawberry.app.core.context.employee.utils;

import static com.strawberry.app.core.context.common.utils.read.event.Util.getModifiedAt;
import static com.strawberry.app.core.context.common.utils.read.event.Util.getModifiedBy;

import com.strawberry.app.core.context.employee.command.StrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeFailedEvent;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrawberryEmployeeEventBuilderUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(StrawberryEmployeeEventBuilderUtils.class);

  public static StrawberryEmployeeEvent buildFailedEvent(StrawberryEmployeeCommand command, List<String> messages) {
    StrawberryEmployeeEvent event = StrawberryEmployeeFailedEvent.builder()
        .from(command)
        .messages(messages)
        .modifiedAt(getModifiedAt(command))
        .modifiedBy(getModifiedBy(command))
        .build();
    LOGGER.info("{}(identity={}) failed. Produce failed event: {}", command.getClass().getSimpleName(), command.identity().value(), event);
    return event;
  }
}
