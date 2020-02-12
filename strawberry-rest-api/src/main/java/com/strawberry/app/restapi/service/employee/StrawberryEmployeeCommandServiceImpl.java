package com.strawberry.app.restapi.service.employee;

import com.strawberry.app.core.context.employee.command.AddStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeNoteCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeRoleCommand;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrawberryEmployeeCommandServiceImpl implements StrawberryEmployeeCommandService {

  CommandGateway commandGateway;

  public StrawberryEmployeeCommandServiceImpl(CommandGateway commandGateway) {
    this.commandGateway = commandGateway;
  }

  @Override
  public Mono<String> createEmployee(AddStrawberryEmployeeCommand addStrawberryEmployeeCommand) {
    return Mono.fromFuture(commandGateway.send(addStrawberryEmployeeCommand));
  }

  @Override
  public Mono<String> amendEmployee(AmendStrawberryEmployeeCommand amendStrawberryEmployeeCommand) {
    return Mono.fromFuture(commandGateway.send(amendStrawberryEmployeeCommand));
  }

  @Override
  public Mono<String> amendEmployeeNote(AmendStrawberryEmployeeNoteCommand amendStrawberryEmployeeNoteCommand) {
    return Mono.fromFuture(commandGateway.send(amendStrawberryEmployeeNoteCommand));
  }

  @Override
  public Mono<String> amendEmployeeRole(AmendStrawberryEmployeeRoleCommand amendStrawberryEmployeeRoleCommand) {
    return Mono.fromFuture(commandGateway.send(amendStrawberryEmployeeRoleCommand));
  }
}
