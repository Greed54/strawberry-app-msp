package com.strawberry.app.core.context.workday.properties;

import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import java.util.HashSet;
import org.immutables.value.Value.Default;

public interface HasStrawberryWorkDayTeams {

  @Default
  default HashSet<StrawberryTeamId> teamIds() {
    return new HashSet<>();
  }
}
