package com.strawberry.app.core.context.team.properties;

import com.strawberry.app.common.annotation.InjectTargetAggregateIdentifier;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;

public interface HasStrawberryTeamId {

  @InjectTargetAggregateIdentifier
  StrawberryTeamId identity();
}
