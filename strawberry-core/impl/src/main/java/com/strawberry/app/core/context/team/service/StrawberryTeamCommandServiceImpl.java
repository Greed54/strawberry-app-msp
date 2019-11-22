package com.strawberry.app.core.context.team.service;

import com.strawberry.app.core.context.team.command.AddStrawberryTeamCommand;
import com.strawberry.app.core.context.team.command.AmendStrawberryTeamCommand;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrawberryTeamCommandServiceImpl implements StrawberryTeamCommandService {

  CommandGateway commandGateway;

  public StrawberryTeamCommandServiceImpl(CommandGateway commandGateway) {
    this.commandGateway = commandGateway;
  }

  @Override
  public CompletableFuture<String> createTeam(AddStrawberryTeamCommand addStrawberryTeamCommand) {
    return commandGateway.send(addStrawberryTeamCommand);
  }

  @Override
  public CompletableFuture<String> amendTeam(AmendStrawberryTeamCommand amendStrawberryTeamCommand) {
    return commandGateway.send(amendStrawberryTeamCommand);
  }
}
