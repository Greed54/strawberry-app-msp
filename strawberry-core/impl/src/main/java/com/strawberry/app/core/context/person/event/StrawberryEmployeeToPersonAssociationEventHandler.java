package com.strawberry.app.core.context.person.event;

import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAddedEvent;
import com.strawberry.app.core.context.person.StrawberryPerson;
import com.strawberry.app.core.context.person.command.AddStrawberryPersonCommand;
import com.strawberry.app.core.context.person.service.StrawberryPersonService;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrawberryEmployeeToPersonAssociationEventHandler {

  StrawberryPersonService personService;
  CommandGateway commandGateway;

  @EventHandler
  public void handleEx(StrawberryEmployeeAddedEvent employeeAddedEvent) {
    Optional<StrawberryPerson> person = personService.getPerson(employeeAddedEvent.personId());
    if (person.isEmpty()) {
      AddStrawberryPersonCommand addStrawberryPersonCommand = AddStrawberryPersonCommand.builder()
          .identity(employeeAddedEvent.personId())
          .firstName(employeeAddedEvent.firstName())
          .lastName(employeeAddedEvent.lastName())
          .userName(employeeAddedEvent.firstName() + '.' + employeeAddedEvent.lastName())
          .isAdmin(false)
          .createdAt(employeeAddedEvent.createdAt())
          .build();

      commandGateway.send(addStrawberryPersonCommand);
    } else {
      log.info("Skip add person with identity {} to Employee", employeeAddedEvent.personId());
    }
  }
}
