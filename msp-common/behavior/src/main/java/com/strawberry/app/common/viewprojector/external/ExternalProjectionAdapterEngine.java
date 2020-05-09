package com.strawberry.app.common.viewprojector.external;

import com.strawberry.app.common.Identity;
import com.strawberry.app.common.projection.ProjectionEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExternalProjectionAdapterEngine {

  Map<Class, ExternalAbstractProjectionAdapter> classToPEventProjectionAdapter;

  public ExternalProjectionAdapterEngine(Collection<ExternalAbstractProjectionAdapter> projectionAdapters) {
    this.classToPEventProjectionAdapter = new HashMap<>();

    projectionAdapters.forEach(adapter -> {
      Class projectionEventClass = adapter.eventStream().getValueClass();
      ExternalAbstractProjectionAdapter oldVal = classToPEventProjectionAdapter.put(projectionEventClass, adapter);
      if (oldVal != null) {
        throw new IllegalArgumentException();
      }
    });
  }

  @SuppressWarnings("unchecked")
  public <K extends Identity<?>, PE extends ProjectionEvent<K>> ExternalAbstractProjectionAdapter<K, PE> getAdapter(Class clazz) {
    return (ExternalAbstractProjectionAdapter<K, PE>) classToPEventProjectionAdapter.get(clazz);
  }
}
