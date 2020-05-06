package com.strawberry.app.common.event;

import org.springframework.context.ApplicationEvent;

public class MspApplicationRunningEvent extends ApplicationEvent {

  public MspApplicationRunningEvent(Object source) {
    super(source);
  }
}
