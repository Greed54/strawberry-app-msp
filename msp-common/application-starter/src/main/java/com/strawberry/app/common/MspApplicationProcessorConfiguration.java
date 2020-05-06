package com.strawberry.app.common;

import com.strawberry.app.common.event.MspApplicationRebalancingStartedEvent;
import com.strawberry.app.common.event.MspApplicationRebalancingStoppedEvent;
import com.strawberry.app.common.event.MspApplicationStartedEvent;
import com.strawberry.app.common.event.MspRebalancingSchedulerStartedEvent;
import com.strawberry.app.common.event.MspRebalancingSchedulerStoppedEvent;
import java.util.Objects;
import java.util.OptionalLong;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.axonframework.eventhandling.TrackingToken;
import org.axonframework.messaging.StreamableMessageSource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Slf4j
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MspApplicationProcessorConfiguration {

  public static final String PROJECTION_PROCESSOR_GROUP_NAME = "projection-adapters";

  final EventProcessingConfiguration processingConfiguration;
  final ApplicationEventPublisher applicationEventPublisher;

  ScheduledExecutorService executorService;

  public MspApplicationProcessorConfiguration(EventProcessingConfiguration processingConfiguration,
      ApplicationEventPublisher applicationEventPublisher) {
    this.processingConfiguration = processingConfiguration;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @EventListener
  public void applicationStarted(MspApplicationStartedEvent event) {
    processingConfiguration.eventProcessorByProcessingGroup(PROJECTION_PROCESSOR_GROUP_NAME, TrackingEventProcessor.class)
        .ifPresent(TrackingEventProcessor::shutDown);
    executorService = Executors.newSingleThreadScheduledExecutor();

    executorService.schedule(() ->
        applicationEventPublisher.publishEvent(new MspApplicationRebalancingStartedEvent(event)), 2, TimeUnit.SECONDS);
  }

  @EventListener
  public void rebalancingStarted(MspApplicationRebalancingStartedEvent event) {
    executorService.shutdown();
    processingConfiguration.eventProcessorByProcessingGroup(PROJECTION_PROCESSOR_GROUP_NAME, TrackingEventProcessor.class)
        .ifPresent(trackingEventProcessor -> {
          trackingEventProcessor.resetTokens(StreamableMessageSource::createTailToken);
          trackingEventProcessor.start();
        });
    applicationEventPublisher.publishEvent(new MspRebalancingSchedulerStartedEvent(event));
  }

  @EventListener
  public void rebalancingStopped(MspRebalancingSchedulerStoppedEvent event) {
    executorService.shutdown();
    processingConfiguration.eventProcessorByProcessingGroup(PROJECTION_PROCESSOR_GROUP_NAME, TrackingEventProcessor.class)
        .ifPresent(TrackingEventProcessor::shutDown);
    log.info("Rebalancing Scheduler stopped");
    applicationEventPublisher.publishEvent(new MspApplicationRebalancingStoppedEvent(event));
  }

  @EventListener
  public void startRebalancingScheduler(MspRebalancingSchedulerStartedEvent event) {
    executorService = Executors.newSingleThreadScheduledExecutor();
    Runnable runnableTask = () -> processingConfiguration
        .eventProcessorByProcessingGroup(PROJECTION_PROCESSOR_GROUP_NAME, TrackingEventProcessor.class)
        .ifPresent(trackingEventProcessor -> {
          OptionalLong headTokenPosition = trackingEventProcessor.getMessageSource().createHeadToken().position();
          trackingEventProcessor.processingStatus()
              .forEach((integer, eventTrackerStatus) -> {
                    TrackingToken trackingToken = eventTrackerStatus.getTrackingToken();
                    if (headTokenPosition.isPresent() && Objects.nonNull(trackingToken) && trackingToken.position().isPresent()) {
                      if (trackingToken.position().getAsLong() == headTokenPosition.getAsLong()) {
                        applicationEventPublisher.publishEvent(new MspRebalancingSchedulerStoppedEvent(event));
                      }
                    }
                  }
              );

        });
    executorService.scheduleAtFixedRate(runnableTask, 0, 100, TimeUnit.MILLISECONDS);
    log.info("Rebalancing Scheduler started");
  }
}
