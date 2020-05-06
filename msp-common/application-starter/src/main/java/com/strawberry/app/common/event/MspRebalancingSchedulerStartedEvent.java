package com.strawberry.app.common.event;

import org.springframework.context.ApplicationEvent;

public class MspRebalancingSchedulerStartedEvent extends ApplicationEvent {

  public MspRebalancingSchedulerStartedEvent(Object source) {
    super(source);
  }
}
