package com.strawberry.app.common.behavior;

import com.strawberry.app.common.Command;
import com.strawberry.app.common.Identity;
import com.strawberry.app.common.cqengine.RepositoryFactory;
import com.strawberry.app.common.event.BusinessEvent;
import com.strawberry.app.common.projection.Projection;
import com.strawberry.app.common.projection.State;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class BehaviorEngine {

  protected RepositoryFactory factory;

  private Map<Class, Behavior> classToCommandBehavior;

  private Map<Class, Behavior> classToEventBehavior;

  @SuppressWarnings("unchecked")
  protected BehaviorEngine(RepositoryFactory factory, Collection<Behavior> behaviors) {
    this.factory = factory;
    this.classToCommandBehavior = new HashMap<>();
    this.classToEventBehavior = new HashMap<>();

    behaviors.forEach(behavior -> {
      final Collection<Class> supportedCommands = behavior.getSupportedCommands();
      supportedCommands.forEach(commandType -> {
        Behavior oldVal = classToCommandBehavior.put(commandType, behavior);
        if (oldVal != null) {
          throw new IllegalArgumentException();
        }
      });
      final Collection<Class> supportedEvents = behavior.getSupportedEvents();
      supportedEvents.forEach(eventType -> {
        Behavior oldVal = classToEventBehavior.put(eventType, behavior);
        if (oldVal != null) {
          throw new IllegalArgumentException();
        }
      });
    });
  }

  @SuppressWarnings("unchecked")
  public <K extends Identity<?>,
      E extends BusinessEvent<K>,
      C extends Command<K>,
      S extends State<K>> Behavior<K, E, C, S> getBehavior(Class clazz) {

    if (Command.class.isAssignableFrom(clazz)) {
      return (Behavior<K, E, C, S>) classToCommandBehavior.get(clazz);
    } else if (BusinessEvent.class.isAssignableFrom(clazz)) {
      return (Behavior<K, E, C, S>) classToEventBehavior.get(clazz);
    }

    throw new IllegalArgumentException("No behavior associated with " + clazz);
  }

  protected abstract <K extends Identity<?>, P extends Projection<K>> void project(P projection);

  public <K extends Identity<?>, S extends State<K>> S get(K id, Class<S> clazz) {
    return factory.getStateStore(clazz).get(id);
  }


}
