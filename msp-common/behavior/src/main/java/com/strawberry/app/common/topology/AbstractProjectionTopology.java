package com.strawberry.app.common.topology;

import com.strawberry.app.common.projection.ProjectionEvent;

public interface AbstractProjectionTopology {

  void process(ProjectionEvent projectionEvent);

  String topologyName();

}
