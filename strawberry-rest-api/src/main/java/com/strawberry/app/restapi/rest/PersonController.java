package com.strawberry.app.restapi.rest;

import com.strawberry.app.core.context.person.command.AddStrawberryPersonCommand;
import com.strawberry.app.restapi.service.person.StrawberryPersonCommandService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class PersonController {

  StrawberryPersonCommandService commandService;

  @PostMapping("api/addPerson")
  public String addPerson(@RequestBody AddStrawberryPersonCommand addStrawberryPersonCommand) {
    return commandService.createPerson(addStrawberryPersonCommand).toString();
  }
}
