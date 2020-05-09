package com.strawberry.app.core.context.person.properties;

import com.strawberry.app.common.annotation.InjectTargetAggregateIdentifier;
import com.strawberry.app.core.context.person.identities.StrawberryPersonId;

public interface HasStrawberryPersonId {

  @InjectTargetAggregateIdentifier
  StrawberryPersonId identity();
}
