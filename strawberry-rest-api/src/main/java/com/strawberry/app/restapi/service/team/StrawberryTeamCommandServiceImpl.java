package com.strawberry.app.restapi.service.team;

import com.strawberry.app.core.context.team.command.AddStrawberryTeamCommand;
import com.strawberry.app.core.context.team.command.AmendStrawberryTeamCommand;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrawberryTeamCommandServiceImpl implements StrawberryTeamCommandService {

  CommandGateway commandGateway;

  public StrawberryTeamCommandServiceImpl(CommandGateway commandGateway) {
    this.commandGateway = commandGateway;
  }

  @Override
  public Mono<String> createTeam(AddStrawberryTeamCommand addStrawberryTeamCommand) {
    return Mono.fromFuture(commandGateway.send(addStrawberryTeamCommand));
  }

  @Override
  public Mono<String> amendTeam(AmendStrawberryTeamCommand amendStrawberryTeamCommand) {
    return Mono.fromFuture(commandGateway.send(amendStrawberryTeamCommand));
  }
}
