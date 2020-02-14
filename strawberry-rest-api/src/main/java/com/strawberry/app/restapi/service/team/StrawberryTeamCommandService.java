package com.strawberry.app.restapi.service.team;

import com.strawberry.app.core.context.team.command.AddStrawberryTeamCommand;
import com.strawberry.app.core.context.team.command.AmendStrawberryTeamCommand;
import reactor.core.publisher.Mono;

public interface StrawberryTeamCommandService {

  Mono<String> createTeam(AddStrawberryTeamCommand addStrawberryTeamCommand);

  Mono<String> amendTeam(AmendStrawberryTeamCommand amendStrawberryTeamCommand);
}
