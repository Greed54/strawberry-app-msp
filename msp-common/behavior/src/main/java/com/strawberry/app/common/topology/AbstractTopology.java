package com.strawberry.app.common.topology;

import com.google.common.collect.ImmutableSet;
import com.strawberry.app.common.Identity;
import com.strawberry.app.common.cqengine.ProjectionIndex;
import com.strawberry.app.common.projection.ProjectionEvent;
import com.strawberry.app.common.projection.ProjectionEventStream;
import java.util.function.Function;

public interface AbstractTopology<I extends Identity<?>, PE extends ProjectionEvent<I>> {

  String topologyName();

  default Function<? super PE, I> identityGetter() {
    return PE::identity;
  }

  default ImmutableSet<ProjectionIndex<PE>> indices() {
    return ImmutableSet.of();
  }

  ProjectionEventStream<I, PE> eventStream();
}
