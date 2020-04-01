package com.strawberry.app.core.context.workday.service;

import com.googlecode.cqengine.query.QueryFactory;
import com.strawberry.app.core.context.utils.service.RepositoryService;
import com.strawberry.app.core.context.workday.IStrawberryWorkDay.Attributes;
import com.strawberry.app.core.context.workday.StrawberryWorkDay;
import com.strawberry.app.core.context.workday.identities.StrawberryWorkDayId;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class StrawberryWorkDayService {

  RepositoryService repositoryService;

  public Optional<StrawberryWorkDay> getWorkDay(StrawberryWorkDayId workDayId) {
    return repositoryService.retrieveState(workDayId, StrawberryWorkDay.class);
  }

  public Optional<StrawberryWorkDay> getActiveStrawberryWorkDay(StrawberryWorkDayId workDayId) {
    return getWorkDay(workDayId)
        .filter(workDay -> !workDay.removed());
  }

  public Optional<StrawberryWorkDay> getNowStrawberryWorkDay() {
    return repositoryService
        .retrieveUnique(QueryFactory.equal(Attributes.WORK_DAY_DATE, Instant.now().truncatedTo(ChronoUnit.DAYS)), StrawberryWorkDay.class);
  }
}
