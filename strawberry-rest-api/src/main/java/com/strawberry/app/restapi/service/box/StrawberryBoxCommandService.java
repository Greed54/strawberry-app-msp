package com.strawberry.app.restapi.service.box;

import com.strawberry.app.core.context.box.command.AddStrawberryBoxCommand;
import reactor.core.publisher.Mono;

public interface StrawberryBoxCommandService {

  Mono<String> addBox(AddStrawberryBoxCommand addStrawberryBoxCommand);
}
