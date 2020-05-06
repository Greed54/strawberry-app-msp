package com.strawberry.app.common.event;

import org.springframework.context.ApplicationEvent;

public class MspApplicationErrorEvent extends ApplicationEvent {

  public MspApplicationErrorEvent(Object source) {
    super(source);
  }
}
