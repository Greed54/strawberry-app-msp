package com.strawberry.app.core.context.team.service;

import com.strawberry.app.core.context.team.command.AddStrawberryTeamCommand;
import com.strawberry.app.core.context.team.command.AmendStrawberryTeamCommand;
import java.util.concurrent.CompletableFuture;

public interface StrawberryTeamCommandService {

  CompletableFuture<String> createTeam(AddStrawberryTeamCommand addStrawberryTeamCommand);
  CompletableFuture<String> amendTeam(AmendStrawberryTeamCommand amendStrawberryTeamCommand);
}
