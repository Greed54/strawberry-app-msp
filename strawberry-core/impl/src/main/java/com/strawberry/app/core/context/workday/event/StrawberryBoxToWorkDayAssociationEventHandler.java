package com.strawberry.app.core.context.workday.event;

import com.strawberry.app.common.property.context.identity.PersonId;
import com.strawberry.app.core.context.box.event.StrawberryBoxAddedEvent;
import com.strawberry.app.core.context.employee.StrawberryEmployee;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.service.StrawberryEmployeeService;
import com.strawberry.app.core.context.team.StrawberryTeam;
import com.strawberry.app.core.context.team.service.StrawberryTeamService;
import com.strawberry.app.core.context.workday.StrawberryWorkDay;
import com.strawberry.app.core.context.workday.aggregate.StrawberryWorkDayAggregate;
import com.strawberry.app.core.context.workday.command.AddStrawberryWorkDayTeamCommand;
import com.strawberry.app.core.context.workday.service.StrawberryWorkDayService;
import java.time.Instant;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.GenericDomainEventMessage;
import org.axonframework.eventsourcing.eventstore.EventStore;
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
  CommandGateway commandGateway;

  Logger LOGGER = LoggerFactory.getLogger(StrawberryBoxToWorkDayAssociationEventHandler.class);

  public StrawberryBoxToWorkDayAssociationEventHandler(StrawberryWorkDayService workDayService,
      StrawberryTeamService strawberryTeamService,
      StrawberryEmployeeService strawberryEmployeeService,
      CommandGateway commandGateway) {
    this.workDayService = workDayService;
    this.strawberryTeamService = strawberryTeamService;
    this.strawberryEmployeeService = strawberryEmployeeService;
    this.commandGateway = commandGateway;
  }

  @EventHandler
  public void handleEx(StrawberryBoxAddedEvent boxAddedEvent) {
    Optional<StrawberryWorkDay> nowStrawberryWorkDay = workDayService.getNowStrawberryWorkDay();
    nowStrawberryWorkDay.ifPresent(strawberryWorkDay -> addTeamToWorkDay(strawberryWorkDay, boxAddedEvent.employeeId()));
  }

  private void addTeamToWorkDay(StrawberryWorkDay strawberryWorkDay, StrawberryEmployeeId employeeId) {

    Optional<StrawberryTeam> strawberryTeam = strawberryEmployeeService.getEmployee(employeeId).stream()
        .map(StrawberryEmployee::teamId)
        .map(id -> strawberryTeamService.getTeamOrThrow(id))
        .findFirst();

    strawberryTeam.ifPresent(team -> {
      if (!strawberryWorkDay.teamIds().contains(team.identity())) {
        AddStrawberryWorkDayTeamCommand addStrawberryWorkDayTeamCommand = AddStrawberryWorkDayTeamCommand.builder()
            .identity(strawberryWorkDay.identity())
            .teamId(team.identity())
            .modifiedAt(Instant.now()) //TODO: ModifiedBy
            .modifiedBy(PersonId.next())
            .build();

        commandGateway.send(addStrawberryWorkDayTeamCommand);
      } else {
        LOGGER.debug("Skip add team with identity {} to WorkDay with identity {}", team.identity(), strawberryWorkDay.identity());
      }
    });
  }
}
