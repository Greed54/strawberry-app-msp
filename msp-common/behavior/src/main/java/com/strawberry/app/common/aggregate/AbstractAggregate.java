package com.strawberry.app.common.aggregate;

import com.google.common.collect.ImmutableSet;
import com.strawberry.app.common.Command;
import com.strawberry.app.common.Identity;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.cqengine.ProjectionIndex;
import com.strawberry.app.common.event.BusinessEvent;
import com.strawberry.app.common.projection.State;
import java.util.function.Function;

public interface AbstractAggregate<I extends Identity<?>, C extends Command<I>, E extends BusinessEvent<I>, S extends State<I>> {

  void handleEvent(E businessEvent, DefaultBehaviorEngine behaviorEngine);

  Class<I> identityClass();

  Class<S> stateClass();

  default ImmutableSet<ProjectionIndex<S>> indices() {
    return ImmutableSet.of();
  }

  default Function<? super S, I> identityGetter() {
    return S::identity;
  }

  default void projectState(S state, DefaultBehaviorEngine behaviorEngine) {
    behaviorEngine.project(state);
  }

  void publishProjectionEvent(S state);
}
