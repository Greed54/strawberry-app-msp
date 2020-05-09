package com.strawberry.app.restapi.service.person;

import com.strawberry.app.core.context.person.command.AddStrawberryPersonCommand;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrawberryPersonCommandServiceImpl implements StrawberryPersonCommandService {

  CommandGateway commandGateway;

  @Override
  public Mono<String> createPerson(AddStrawberryPersonCommand addStrawberryPersonCommand) {
    return Mono.fromFuture(commandGateway.send(addStrawberryPersonCommand));
  }
}
