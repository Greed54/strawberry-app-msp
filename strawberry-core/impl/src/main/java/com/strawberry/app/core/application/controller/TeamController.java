package com.strawberry.app.core.application.controller;

import com.strawberry.app.core.context.team.command.AddStrawberryTeamCommand;
import com.strawberry.app.core.context.team.command.AmendStrawberryTeamCommand;
import com.strawberry.app.core.context.team.service.StrawberryTeamCommandServiceImpl;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class TeamController {

  StrawberryTeamCommandServiceImpl commandService;

  @PostMapping("api/addTeam")
  public CompletableFuture<String> addTeam(@RequestBody AddStrawberryTeamCommand addStrawberryTeamCommand) {
    return commandService.createTeam(addStrawberryTeamCommand);
  }

  @PostMapping("api/amendTeam")
  public CompletableFuture<String> amendTeam(@RequestBody AmendStrawberryTeamCommand amendStrawberryTeamCommand) {
    return commandService.amendTeam(amendStrawberryTeamCommand);
  }
}
