package com.strawberry.app.common.cqengine.indexedstore;

import com.strawberry.app.common.Identity;
import com.strawberry.app.common.cqengine.ProjectionIndex;
import com.strawberry.app.common.projection.ProjectionEvent;
import com.strawberry.app.common.projection.ProjectionEventStream;
import java.util.Set;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IndexedProjectionStoreBuilder<K extends Identity<?>, V extends ProjectionEvent<K>> {

  protected final String name;
  final ProjectionEventStream<K, V> eventStream;
  final Function<? super V, K> identityGetter;
  final Set<ProjectionIndex<V>> indices;

  public IndexedStore<K, V> build() {
    return new IndexedStoreImpl<>(name, eventStream.getKeyClass(), eventStream.getValueClass(), indices, identityGetter);
  }
}
