package com.strawberry.app.common.cqengine.indexedstore;

import com.strawberry.app.common.Identity;
import com.strawberry.app.common.cqengine.ProjectionIndex;
import com.strawberry.app.common.projection.State;
import java.util.Set;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IndexedStoreBuilder<K extends Identity<?>, V extends State<K>> {

  protected final String name;
  final Class<K> kClass;
  final Class<V> vClass;
  final Function<? super V, K> identityGetter;
  final Set<ProjectionIndex<V>> indices;

  public IndexedStore<K, V> build() {
    return new IndexedStoreImpl<>(name, kClass, vClass, indices, identityGetter);
  }
}
