package com.strawberry.app.common;

import com.strawberry.app.common.event.MspApplicationErrorEvent;
import com.strawberry.app.common.event.MspApplicationRebalancingStartedEvent;
import com.strawberry.app.common.event.MspApplicationRebalancingStoppedEvent;
import com.strawberry.app.common.event.MspApplicationRunningEvent;
import com.strawberry.app.common.event.MspApplicationStartedEvent;
import com.strawberry.app.common.event.MspApplicationStatuses;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MspApplicationEventListener {

  final ApplicationEventPublisher applicationEventPublisher;
  MspApplicationStatuses status;
  static final String messagePattern = "Handled {}, application state changed from {} to {}";

  public MspApplicationEventListener(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @SneakyThrows
  @EventListener
  public void startMspApplication(ApplicationStartedEvent event) {
    log.info("Handled {} application state changed to {}", event.getClass().getSimpleName(), MspApplicationStatuses.STARTED);
    status = MspApplicationStatuses.STARTED;
    applicationEventPublisher.publishEvent(new MspApplicationStartedEvent(event));
  }

  @EventListener
  public void rebalancingMspApplication(MspApplicationRebalancingStartedEvent event) {
    log.info(messagePattern, event.getClass().getSimpleName(), MspApplicationStatuses.STARTED, MspApplicationStatuses.REBALANCING);
    status = MspApplicationStatuses.REBALANCING;
  }

  @EventListener
  public void runningMspApplication(MspApplicationRebalancingStoppedEvent event) {
    log.info(messagePattern, event.getClass().getSimpleName(), MspApplicationStatuses.REBALANCING, MspApplicationStatuses.RUNNING);
    status = MspApplicationStatuses.RUNNING;
    applicationEventPublisher.publishEvent(new MspApplicationRunningEvent(event));
  }

  @EventListener
  public void runningMspApplication(MspApplicationErrorEvent event) {
    log.info(messagePattern, event.getClass().getSimpleName(), status, MspApplicationStatuses.ERROR);
    status = MspApplicationStatuses.ERROR;
  }

}
