package com.strawberry.app.core.context.common.behavior;

import com.strawberry.app.core.context.common.cqengine.RepositoryFactory;
import com.strawberry.app.core.context.cqrscommon.Identity;
import com.strawberry.app.core.context.cqrscommon.projection.Projection;
import com.strawberry.app.core.context.cqrscommon.projection.ProjectionEvent;
import com.strawberry.app.core.context.team.projection.StrawberryTeamProjectionEvent;
import com.strawberry.app.core.context.team.projection.StrawberryTeamProjectionEvent.Builder;
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
