package com.strawberry.app.core.context.workday.properties;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.immutables.value.Value.Default;

public interface HasStrawberryWorkDayDate {

  @Default
  default Instant date() {
    return Instant.now().truncatedTo(ChronoUnit.DAYS);
  }
}
