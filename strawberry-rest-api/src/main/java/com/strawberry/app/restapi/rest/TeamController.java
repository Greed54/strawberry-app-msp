package com.strawberry.app.restapi.rest;

import com.strawberry.app.core.context.team.command.AddStrawberryTeamCommand;
import com.strawberry.app.core.context.team.command.AmendStrawberryTeamCommand;
import com.strawberry.app.restapi.service.team.StrawberryTeamCommandServiceImpl;
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
public class TeamController {

  StrawberryTeamCommandServiceImpl commandService;

  @PostMapping("api/addTeam")
  public String addTeam(@RequestBody AddStrawberryTeamCommand addStrawberryTeamCommand) {
    return commandService.createTeam(addStrawberryTeamCommand).toString();
  }

  @PostMapping("api/amendTeam")
  public Mono<String> amendTeam(@RequestBody AmendStrawberryTeamCommand amendStrawberryTeamCommand) {
    return commandService.amendTeam(amendStrawberryTeamCommand);
  }
}
