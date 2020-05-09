package com.strawberry.app.read.context.projection;

import static com.strawberry.app.common.ProcessorGroupNames.PROJECTION_PROCESSOR_GROUP_NAME;

import com.strawberry.app.common.projection.ProjectionEvent;
import com.strawberry.app.common.topology.AbstractProjectionTopology;
import com.strawberry.app.read.context.utils.RepositoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
@ProcessingGroup(PROJECTION_PROCESSOR_GROUP_NAME)
public class ProjectionTopology implements AbstractProjectionTopology {

  RepositoryService repositoryService;

  @EventHandler
  @Override
  public void process(ProjectionEvent projectionEvent) {
    repositoryService.saveProjection(projectionEvent.identity(), projectionEvent);
    log.info("Stored {}(identity={}), value: {}", projectionEvent.getClass().getSimpleName(), projectionEvent.identity(), projectionEvent);
  }

  @Override
  public String topologyName() {
    return PROJECTION_PROCESSOR_GROUP_NAME;
  }
}
