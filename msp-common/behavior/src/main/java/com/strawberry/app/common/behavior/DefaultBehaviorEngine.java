package com.strawberry.app.common.behavior;

import com.strawberry.app.common.Identity;
import com.strawberry.app.common.cqengine.RepositoryFactory;
import com.strawberry.app.common.projection.Projection;
import java.util.Collection;

public class DefaultBehaviorEngine extends BehaviorEngine {

  public DefaultBehaviorEngine(RepositoryFactory factory, Collection<Behavior> behaviors) {
    super(factory, behaviors);
  }

  @Override
  public <K extends Identity<?>, P extends Projection<K>> void project(P projection) {
    factory.getStateStore(projection.getClass()).put(projection.identity(), projection);
  }
}
