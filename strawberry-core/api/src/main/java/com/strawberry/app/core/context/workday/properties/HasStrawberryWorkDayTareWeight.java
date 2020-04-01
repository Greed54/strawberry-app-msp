package com.strawberry.app.core.context.workday.properties;

import org.immutables.value.Value.Default;

public interface HasStrawberryWorkDayTareWeight {

  @Default
  default Double tareWeight() {
    return 0.8;
  }
}
