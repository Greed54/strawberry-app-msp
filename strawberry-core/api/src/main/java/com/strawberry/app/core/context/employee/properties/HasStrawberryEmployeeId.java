package com.strawberry.app.core.context.employee.properties;

import com.strawberry.app.common.annotation.InjectTargetAggregateIdentifier;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;

public interface HasStrawberryEmployeeId {

  @InjectTargetAggregateIdentifier
  StrawberryEmployeeId identity();
}
