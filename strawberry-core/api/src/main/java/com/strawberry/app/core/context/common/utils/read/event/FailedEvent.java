package com.strawberry.app.core.context.common.utils.read.event;

import com.strawberry.app.core.context.common.property.context.HasMessages;
import com.strawberry.app.core.context.common.property.context.modified.HasOptionalModified;
import com.strawberry.app.core.context.cqrscommon.event.ValidationEvent;

public interface FailedEvent extends ValidationEvent, HasMessages, HasOptionalModified {

  @Override
  default String details() {
    return String.join(",", messages());
  }

}
