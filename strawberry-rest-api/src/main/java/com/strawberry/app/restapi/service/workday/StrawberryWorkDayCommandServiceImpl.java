package com.strawberry.app.restapi.service.workday;

import com.strawberry.app.core.context.workday.command.AmendStrawberryWorkDayCommand;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class StrawberryWorkDayCommandServiceImpl implements IStrawberryWorkDayCommandService {

  CommandGateway commandGateway;

  @Override
  public Mono<String> amendWorkDay(AmendStrawberryWorkDayCommand amendStrawberryWorkDayCommand) {
    return Mono.fromFuture(commandGateway.send(amendStrawberryWorkDayCommand));
  }
}
