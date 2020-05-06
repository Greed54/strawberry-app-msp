package com.strawberry.app.common.event;

import org.springframework.context.ApplicationEvent;

public class MspApplicationRebalancingStoppedEvent extends ApplicationEvent {

  public MspApplicationRebalancingStoppedEvent(Object source) {
    super(source);
  }
}
