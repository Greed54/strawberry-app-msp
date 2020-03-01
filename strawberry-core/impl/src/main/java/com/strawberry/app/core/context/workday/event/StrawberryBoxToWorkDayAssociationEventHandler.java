package com.strawberry.app.core.context.workday.event;

import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.property.context.identity.PersonId;
import com.strawberry.app.common.property.context.identity.card.CardId;
import com.strawberry.app.core.context.employee.StrawberryEmployee;
import com.strawberry.app.core.context.employee.service.StrawberryEmployeeService;
import com.strawberry.app.core.context.team.StrawberryTeam;
import com.strawberry.app.core.context.team.service.StrawberryTeamService;
import com.strawberry.app.core.context.workday.StrawberryWorkDay;
import com.strawberry.app.core.context.workday.aggregate.StrawberryWorkDayAggregate;
import com.strawberry.app.core.context.workday.identities.StrawberryWorkDayId;
import com.strawberry.app.core.context.workday.service.StrawberryWorkDayService;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.GenericDomainEventMessage;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StrawberryBoxToWorkDayAssociationEventHandler {

  StrawberryWorkDayService workDayService;
  StrawberryTeamService strawberryTeamService;
  StrawberryEmployeeService strawberryEmployeeService;
  EventStore eventStore;
  EventBus eventBus;

  Logger LOGGER = LoggerFactory.getLogger(StrawberryBoxToWorkDayAssociationEventHandler.class);

  public StrawberryBoxToWorkDayAssociationEventHandler(StrawberryWorkDayService workDayService,
      StrawberryTeamService strawberryTeamService,
      StrawberryEmployeeService strawberryEmployeeService,
      EventStore eventStore, @Qualifier("eventBus") EventBus eventBus) {
    this.workDayService = workDayService;
    this.strawberryTeamService = strawberryTeamService;
    this.strawberryEmployeeService = strawberryEmployeeService;
    this.eventStore = eventStore;
    this.eventBus = eventBus;
  }

  @EventHandler
  public void handleEx(CardId cardId) { //TODO: BoxAddedEvent
    Optional<StrawberryWorkDay> nowStrawberryWorkDay = workDayService.getNowStrawberryWorkDay();
    nowStrawberryWorkDay.ifPresentOrElse(strawberryWorkDay -> addTeamToWorkDay(strawberryWorkDay, cardId), this::initiateStrawberryWorkDay);
  }

  @SneakyThrows
  private void initiateStrawberryWorkDay() {
    StrawberryWorkDayInitiatedEvent workDayInitiatedEvent = StrawberryWorkDayInitiatedEvent.builder()
        .identity(StrawberryWorkDayId.builder().value(UUID.randomUUID().toString()).build())
        .createdAt(Instant.now())
        .build();
    AggregateLifecycle
        .createNew(StrawberryWorkDayAggregate.class, () -> new StrawberryWorkDayAggregate(workDayInitiatedEvent));
  }

  private void addTeamToWorkDay(StrawberryWorkDay strawberryWorkDay, CardId cardId) {
    Optional<Long> sequenceNumber = eventStore.lastSequenceNumberFor(strawberryWorkDay.identity().toString());

    sequenceNumber.ifPresentOrElse(sNumber -> {
      Optional<StrawberryTeam> strawberryTeam = strawberryEmployeeService.getEmployeesByCardId(cardId).stream()
          .map(StrawberryEmployee::teamId)
          .map(id -> strawberryTeamService.getTeamOrThrow(id))
          .findFirst();
      strawberryTeam.ifPresent(team -> {
        if (!strawberryWorkDay.teamIds().contains(team.identity())) {
          StrawberryWorkDayTeamAddedEvent workDayTeamAddedEvent = StrawberryWorkDayTeamAddedEvent.builder()
              .identity(strawberryWorkDay.identity())
              .teamId(team.identity())
              .modifiedAt(Instant.now()) //TODO: ModifiedBy
              .modifiedBy(PersonId.next())
              .build();
          GenericDomainEventMessage<StrawberryWorkDayTeamAddedEvent> eventMessage = new GenericDomainEventMessage<>(
              StrawberryWorkDayAggregate.class.getSimpleName(), strawberryWorkDay.identity().toString(), sNumber + 1, workDayTeamAddedEvent); //TODO: ???

          eventBus.publish(eventMessage);
        } else {
          LOGGER.debug("Skip add team with identity {} to WorkDay with identity {}", team.identity(), strawberryWorkDay.identity());
        }
      });
    }, () -> LOGGER.error("Work day with identity {} does not exist", strawberryWorkDay.identity()));
  }
}
