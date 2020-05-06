package com.strawberry.app.common.event;

import org.springframework.context.ApplicationEvent;

public class MspApplicationStartedEvent extends ApplicationEvent {

  public MspApplicationStartedEvent(Object source) {
    super(source);
  }
}
