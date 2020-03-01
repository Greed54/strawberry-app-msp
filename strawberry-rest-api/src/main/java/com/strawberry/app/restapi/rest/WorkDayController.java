package com.strawberry.app.restapi.rest;

import com.strawberry.app.core.context.workday.command.AmendStrawberryWorkDayCommand;
import com.strawberry.app.restapi.service.workday.StrawberryWorkDayCommandServiceImpl;
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
public class WorkDayController {

  StrawberryWorkDayCommandServiceImpl commandService;

  @PostMapping("api/amendWorkDay")
  public Mono<String> amendWorkDay(@RequestBody AmendStrawberryWorkDayCommand amendStrawberryWorkDayCommand) {
    return commandService.amendWorkDay(amendStrawberryWorkDayCommand);
  }
}
