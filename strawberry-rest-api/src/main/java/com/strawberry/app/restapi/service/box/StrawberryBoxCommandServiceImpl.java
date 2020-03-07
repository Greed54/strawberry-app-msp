package com.strawberry.app.restapi.service.box;

import com.strawberry.app.core.context.box.command.AddStrawberryBoxCommand;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class StrawberryBoxCommandServiceImpl implements StrawberryBoxCommandService {

  CommandGateway commandGateway;

  @Override
  public Mono<String> addBox(AddStrawberryBoxCommand addStrawberryBoxCommand) {
    return Mono.fromFuture(commandGateway.send(addStrawberryBoxCommand));
  }
}
