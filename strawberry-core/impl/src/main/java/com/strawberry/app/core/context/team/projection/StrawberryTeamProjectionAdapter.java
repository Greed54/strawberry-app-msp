package com.strawberry.app.core.context.team.projection;

import com.strawberry.app.core.context.team.service.StrawberryTeamProjectionService;
import com.strawberry.app.core.context.utils.Util;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrawberryTeamProjectionAdapter {

  StrawberryTeamProjectionService teamProjectionService;
  Logger LOGGER = LoggerFactory.getLogger(StrawberryTeamProjectionAdapter.class);

  public StrawberryTeamProjectionAdapter(StrawberryTeamProjectionService teamProjectionService) {
    this.teamProjectionService = teamProjectionService;
  }

  @EventHandler
  public void on(StrawberryTeamProjectionEvent event) {
    StrawberryTeamProjectionEntity strawberryTeamProjectionEntity = teamProjectionService.saveProjection(Util.mapTeamProjectionEvent(event));

    LOGGER.info("Projected {}(identity={}), value: {}", event.getClass().getSimpleName(), strawberryTeamProjectionEntity.getIdentity(), event);
  }
}
