package com.strawberry.app.core.context.team.service;

import com.strawberry.app.core.context.team.StrawberryTeam;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.utils.service.RepositoryService;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class StrawberryTeamService {

  RepositoryService repositoryService;

  public Optional<StrawberryTeam> getStrawberryTeam(StrawberryTeamId strawberryTeamId) {
    return repositoryService.retrieveState(strawberryTeamId, StrawberryTeam.class);
  }

  public Optional<StrawberryTeam> getActiveStrawberryTeam(StrawberryTeamId strawberryTeamId) {
    return getStrawberryTeam(strawberryTeamId)
        .filter(strawberryTeam -> !strawberryTeam.removed());
  }

  public StrawberryTeam getTeamOrThrow(StrawberryTeamId strawberryTeamId) {
    return getActiveStrawberryTeam(strawberryTeamId)
        .orElseThrow(() -> new IllegalStateException(String.format("StrawberryTeam %s not present", strawberryTeamId)));
  }
}
