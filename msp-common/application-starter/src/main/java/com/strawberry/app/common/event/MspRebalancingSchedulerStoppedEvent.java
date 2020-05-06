package com.strawberry.app.common.event;

import org.springframework.context.ApplicationEvent;

public class MspRebalancingSchedulerStoppedEvent extends ApplicationEvent {

  public MspRebalancingSchedulerStoppedEvent(Object source) {
    super(source);
  }
}
