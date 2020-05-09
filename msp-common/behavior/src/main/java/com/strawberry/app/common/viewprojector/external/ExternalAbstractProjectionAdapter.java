package com.strawberry.app.common.viewprojector.external;

import com.strawberry.app.common.Identity;
import com.strawberry.app.common.projection.ProjectionEvent;
import com.strawberry.app.common.projection.ProjectionEventStream;

public interface ExternalAbstractProjectionAdapter<I extends Identity<?>, PE extends ProjectionEvent<I>> {

  void convert(PE projectionEvent);

  ProjectionEventStream<I, PE> eventStream();
}
