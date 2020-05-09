package com.strawberry.app.common.viewprojector.internal;

import com.strawberry.app.common.Identity;
import com.strawberry.app.common.annotation.SupportedEvents;
import com.strawberry.app.common.event.business.BusinessEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalProjectionAdapterEngine {

  Map<Class, InternalAbstractProjectionAdapter> classToEventProjectionAdapter;

  public InternalProjectionAdapterEngine(Collection<InternalAbstractProjectionAdapter> projectionAdapters) {
    this.classToEventProjectionAdapter = new HashMap<>();

    projectionAdapters.forEach(adapter -> {
      Class businessEventClass = adapter.eventStream().getValueClass();
      SupportedEvents annotation = (SupportedEvents) businessEventClass.getAnnotation(SupportedEvents.class);

      Arrays.asList(annotation.value()).forEach(event -> {
        InternalAbstractProjectionAdapter oldVal = classToEventProjectionAdapter.put(event, adapter);
        if (oldVal != null) {
          throw new IllegalArgumentException();
        }
      });
    });
  }

  public <K extends Identity<?>, BE extends BusinessEvent<K>> InternalAbstractProjectionAdapter<K, BE> getAdapter(Class clazz) {
    return (InternalAbstractProjectionAdapter<K, BE>) classToEventProjectionAdapter.get(clazz);
  }
}
