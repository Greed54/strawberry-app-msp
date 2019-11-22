package com.strawberry.app.core.context.team.properties;

import com.strawberry.app.core.context.cqrscommon.annotation.InjectTargetAggregateIdentifier;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;

public interface HasStrawberryTeamId {

  @InjectTargetAggregateIdentifier
  StrawberryTeamId identity();
}
