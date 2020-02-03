package com.strawberry.app.core.context.common.cqengine.indexedstore;

import com.strawberry.app.core.context.common.cqengine.ProjectionIndex;
import com.strawberry.app.core.context.cqrscommon.Identity;
import com.strawberry.app.core.context.cqrscommon.projection.State;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
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
