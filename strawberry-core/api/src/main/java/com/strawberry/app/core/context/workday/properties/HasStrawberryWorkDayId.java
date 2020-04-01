package com.strawberry.app.core.context.workday.properties;

import com.strawberry.app.common.annotation.InjectTargetAggregateIdentifier;
import com.strawberry.app.core.context.workday.identities.StrawberryWorkDayId;

public interface HasStrawberryWorkDayId {

  @InjectTargetAggregateIdentifier
  StrawberryWorkDayId identity();
}
