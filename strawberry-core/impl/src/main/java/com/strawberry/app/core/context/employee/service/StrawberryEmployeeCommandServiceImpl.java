package com.strawberry.app.core.context.employee.service;

import com.strawberry.app.core.context.employee.command.AddStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeNoteCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeRoleCommand;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrawberryEmployeeCommandServiceImpl implements StrawberryEmployeeCommandService {

  CommandGateway commandGateway;

  public StrawberryEmployeeCommandServiceImpl(CommandGateway commandGateway) {
    this.commandGateway = commandGateway;
  }

  @Override
  public CompletableFuture<String> createEmployee(AddStrawberryEmployeeCommand addStrawberryEmployeeCommand) {
    return commandGateway.send(addStrawberryEmployeeCommand);
  }

  @Override
  public CompletableFuture<String> amendEmployee(AmendStrawberryEmployeeCommand amendStrawberryEmployeeCommand) {
    return commandGateway.send(amendStrawberryEmployeeCommand);
  }

  @Override
  public CompletableFuture<String> amendEmployeeNote(AmendStrawberryEmployeeNoteCommand amendStrawberryEmployeeNoteCommand) {
    return commandGateway.send(amendStrawberryEmployeeNoteCommand);
  }

  @Override
  public CompletableFuture<String> amendEmployeeRole(AmendStrawberryEmployeeRoleCommand amendStrawberryEmployeeRoleCommand) {
    return commandGateway.send(amendStrawberryEmployeeRoleCommand);
  }
}
