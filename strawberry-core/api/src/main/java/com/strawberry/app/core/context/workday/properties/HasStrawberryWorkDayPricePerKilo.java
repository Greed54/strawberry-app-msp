package com.strawberry.app.core.context.workday.properties;

import org.immutables.value.Value.Default;

public interface HasStrawberryWorkDayPricePerKilo {

  @Default
  default Double pricePerKilogram() {
    return 0.0;
  }
}
