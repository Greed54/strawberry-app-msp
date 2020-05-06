package com.strawberry.app.common.event;

import org.springframework.context.ApplicationEvent;

public class MspApplicationRebalancingStartedEvent extends ApplicationEvent {

  public MspApplicationRebalancingStartedEvent(Object source) {
    super(source);
  }
}
