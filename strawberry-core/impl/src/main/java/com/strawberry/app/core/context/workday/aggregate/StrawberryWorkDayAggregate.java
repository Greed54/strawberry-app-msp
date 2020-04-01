package com.strawberry.app.core.context.workday.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import com.google.common.collect.ImmutableSet;
import com.strawberry.app.common.aggregate.IAggregate;
import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.cqengine.ProjectionIndex;
import com.strawberry.app.core.context.workday.StrawberryWorkDay;
import com.strawberry.app.core.context.workday.command.AmendStrawberryWorkDayCommand;
import com.strawberry.app.core.context.workday.command.StrawberryWorkDayCommand;
import com.strawberry.app.core.context.workday.event.StrawberryWorkDayEvent;
import com.strawberry.app.core.context.workday.event.StrawberryWorkDayFailedEvent;
import com.strawberry.app.core.context.workday.event.StrawberryWorkDayInitiatedEvent;
import com.strawberry.app.core.context.workday.identities.StrawberryWorkDayId;
import com.strawberry.app.core.context.workday.projection.StrawberryWorkDayProjectionEvent;
import com.strawberry.app.core.context.workday.properties.HasStrawberryWorkDayId;
import com.strawberry.app.core.context.workday.service.StrawberryWorkDayService;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StrawberryWorkDayAggregate implements
    IAggregate<StrawberryWorkDayId, StrawberryWorkDayCommand, StrawberryWorkDayEvent, StrawberryWorkDay> {

  @AggregateIdentifier
  @Getter
  StrawberryWorkDayId identity;
  @Getter
  StrawberryWorkDay workDay;

  public StrawberryWorkDayAggregate() {
  }

  public StrawberryWorkDayAggregate(StrawberryWorkDayInitiatedEvent workDayInitiatedEvent) {
    apply(workDayInitiatedEvent);
  }

  @CommandHandler
  public void handleEx(AmendStrawberryWorkDayCommand command, DefaultBehaviorEngine defaultBehaviorEngine, StrawberryWorkDayService workDayService) {
    Behavior<StrawberryWorkDayId, StrawberryWorkDayEvent, StrawberryWorkDayCommand, StrawberryWorkDay> behavior = defaultBehaviorEngine
        .getBehavior(command.getClass());
    behavior.commandToEvents(command, workDayService.getWorkDay(command.identity())).forEach(AggregateLifecycle::apply);
  }

  @EventHandler
  @Override
  public void handleEvent(StrawberryWorkDayEvent businessEvent, DefaultBehaviorEngine behaviorEngine) {
    if (!(businessEvent instanceof StrawberryWorkDayFailedEvent)) {
      Behavior<StrawberryWorkDayId, StrawberryWorkDayEvent, StrawberryWorkDayCommand, StrawberryWorkDay> behavior = behaviorEngine
        .getBehavior(businessEvent.getClass());

      this.identity = businessEvent.identity();
      this.workDay = behavior.eventToState(businessEvent, Optional.ofNullable(workDay));

      projectState(workDay, behaviorEngine);
      publishProjectionEvent(workDay);
    } else {
      this.identity = businessEvent.identity();
    }
  }

  @Override
  public Class<StrawberryWorkDayId> identityClass() {
    return StrawberryWorkDayId.class;
  }

  @Override
  public Class<StrawberryWorkDay> stateClass() {
    return StrawberryWorkDay.class;
  }

  @Override
  public ImmutableSet<ProjectionIndex<StrawberryWorkDay>> indices() {
    return StrawberryWorkDay.INDICES;
  }

  @Override
  public void publishProjectionEvent(StrawberryWorkDay state) {
    apply(StrawberryWorkDayProjectionEvent.builder()
        .from((HasStrawberryWorkDayId) state)
        .build());
  }
}
