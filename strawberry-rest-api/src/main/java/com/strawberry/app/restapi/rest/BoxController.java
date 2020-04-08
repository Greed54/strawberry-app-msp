package com.strawberry.app.restapi.rest;

import com.strawberry.app.core.context.box.command.AddStrawberryBoxCommand;
import com.strawberry.app.restapi.service.box.StrawberryBoxCommandServiceImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class BoxController {

  StrawberryBoxCommandServiceImpl commandService;

  @PostMapping("api/addBox")
  public Mono<String> addBox(@RequestBody AddStrawberryBoxCommand addStrawberryBoxCommand) {
    return commandService.addBox(addStrawberryBoxCommand);
  }
}
