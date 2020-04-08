package com.strawberry.app.core.context.box.properties;

import com.strawberry.app.core.context.workday.identities.StrawberryWorkDayId;
import javax.annotation.Nullable;

public interface HasOptionalStrawberryWorkDayId {

  @Nullable
  StrawberryWorkDayId workDayId();
}
