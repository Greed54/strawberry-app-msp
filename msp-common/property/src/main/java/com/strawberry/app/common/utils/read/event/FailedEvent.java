package com.strawberry.app.common.utils.read.event;

import com.strawberry.app.common.property.context.HasMessages;
import com.strawberry.app.common.property.context.modified.HasOptionalModified;
import com.strawberry.app.common.event.ValidationEvent;

public interface FailedEvent extends ValidationEvent, HasMessages, HasOptionalModified {

  @Override
  default String details() {
    return String.join(",", messages());
  }

}
