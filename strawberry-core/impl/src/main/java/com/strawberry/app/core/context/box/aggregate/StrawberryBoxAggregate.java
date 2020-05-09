package com.strawberry.app.core.context.box.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import static org.axonframework.modelling.command.AggregateLifecycle.createNew;

import com.google.common.collect.ImmutableSet;
import com.strawberry.app.common.aggregate.AbstractAggregate;
import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.cqengine.ProjectionIndex;
import com.strawberry.app.common.property.context.identity.PersonId;
import com.strawberry.app.common.property.context.identity.card.CardId;
import com.strawberry.app.core.context.box.StrawberryBox;
import com.strawberry.app.core.context.box.command.AddStrawberryBoxCommand;
import com.strawberry.app.core.context.box.command.StrawberryBoxCommand;
import com.strawberry.app.core.context.box.event.StrawberryBoxEvent;
import com.strawberry.app.core.context.box.event.StrawberryBoxFailedEvent;
import com.strawberry.app.core.context.box.event.StrawberryBoxWorkDayAmendedEvent;
import com.strawberry.app.core.context.box.identities.StrawberryBoxId;
import com.strawberry.app.core.context.box.projecton.StrawberryBoxProjectionEvent;
import com.strawberry.app.core.context.box.properties.HasStrawberryBoxId;
import com.strawberry.app.core.context.box.service.StrawberryBoxService;
import com.strawberry.app.core.context.employee.service.StrawberryEmployeeService;
import com.strawberry.app.core.context.workday.StrawberryWorkDay;
import com.strawberry.app.core.context.workday.aggregate.StrawberryWorkDayAggregate;
import com.strawberry.app.core.context.workday.event.StrawberryWorkDayInitiatedEvent;
import com.strawberry.app.core.context.workday.identities.StrawberryWorkDayId;
import com.strawberry.app.core.context.workday.service.StrawberryWorkDayService;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@FieldDefaults(level = AccessLevel.PRIVATE)
@ProcessingGroup("StrawberryBoxAggregate")
public class StrawberryBoxAggregate implements AbstractAggregate<StrawberryBoxId, StrawberryBoxCommand, StrawberryBoxEvent, StrawberryBox> {

  @AggregateIdentifier
  @Getter
  StrawberryBoxId identity;
  @Getter
  StrawberryBox box;

  public StrawberryBoxAggregate() {
  }

  @CommandHandler
  public StrawberryBoxAggregate(AddStrawberryBoxCommand command, DefaultBehaviorEngine defaultBehaviorEngine, StrawberryBoxService boxService,
      StrawberryWorkDayService workDayService, StrawberryEmployeeService employeeService) {
    Behavior<StrawberryBoxId, StrawberryBoxEvent, StrawberryBoxCommand, StrawberryBox> behavior = defaultBehaviorEngine
        .getBehavior(command.getClass());
    behavior.commandToEvents(command, boxService.getBox(command.identity()))
        .forEach(strawberryBoxEvent -> {
          if (workDayService.getNowStrawberryWorkDay().isEmpty()) {
            initiateStrawberryWorkDay();
          }
          apply(strawberryBoxEvent);
          apply(amendBoxWorkDay(command, workDayService, employeeService));
        });
  }

  @Override
  @EventHandler
  public void handleEvent(StrawberryBoxEvent businessEvent, DefaultBehaviorEngine behaviorEngine) {
    if (!(businessEvent instanceof StrawberryBoxFailedEvent)) {
      Behavior<StrawberryBoxId, StrawberryBoxEvent, StrawberryBoxCommand, StrawberryBox> behavior = behaviorEngine
          .getBehavior(businessEvent.getClass());
      this.identity = businessEvent.identity();
      this.box = behavior.eventToState(businessEvent, Optional.ofNullable(box));

      projectState(box, behaviorEngine);
      publishProjectionEvent(box);
    } else {
      this.identity = businessEvent.identity();
    }
  }

  @Override
  public Class<StrawberryBoxId> identityClass() {
    return StrawberryBoxId.class;
  }

  @Override
  public Class<StrawberryBox> stateClass() {
    return StrawberryBox.class;
  }

  @Override
  public ImmutableSet<ProjectionIndex<StrawberryBox>> indices() {
    return StrawberryBox.INDICES;
  }

  @Override
  public void publishProjectionEvent(StrawberryBox state) {
    apply(StrawberryBoxProjectionEvent.builder()
        .from((HasStrawberryBoxId) state)
        .build());
  }

  @SneakyThrows
  private void initiateStrawberryWorkDay() {
    StrawberryWorkDayInitiatedEvent workDayInitiatedEvent = StrawberryWorkDayInitiatedEvent.builder()
        .identity(StrawberryWorkDayId.builder().value(UUID.randomUUID().toString()).build())
        .createdAt(Instant.now())
        .build();
    createNew(StrawberryWorkDayAggregate.class, () -> new StrawberryWorkDayAggregate(workDayInitiatedEvent));
  }

  private StrawberryBoxWorkDayAmendedEvent amendBoxWorkDay(AddStrawberryBoxCommand command, StrawberryWorkDayService workDayService,
      StrawberryEmployeeService employeeService) {
    Optional<StrawberryWorkDay> nowStrawberryWorkDay = workDayService.getNowStrawberryWorkDay();
    return StrawberryBoxWorkDayAmendedEvent.builder()
        .identity(command.identity())
        .workDayId(nowStrawberryWorkDay.get().identity())
        .modifiedAt(command.createdAt())
        .modifiedBy(new PersonId(employeeService.getEmployeeByCardIdOrThrow(new CardId(command.cardId())).personId().value()))
        .build();
  }
}
