package com.strawberry.app.core.context.box.properties;

import com.strawberry.app.common.annotation.InjectTargetAggregateIdentifier;
import com.strawberry.app.core.context.box.identities.StrawberryBoxId;

public interface HasStrawberryBoxId {

  @InjectTargetAggregateIdentifier
  StrawberryBoxId identity();
}
