package com.strawberry.app.restapi.service.person;

import com.strawberry.app.core.context.person.command.AddStrawberryPersonCommand;
import reactor.core.publisher.Mono;

public interface StrawberryPersonCommandService {

  Mono<String> createPerson(AddStrawberryPersonCommand addStrawberryPersonCommand);
}
