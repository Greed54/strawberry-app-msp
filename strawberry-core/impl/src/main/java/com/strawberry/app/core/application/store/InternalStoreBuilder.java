package com.strawberry.app.core.application.store;

import com.strawberry.app.common.aggregate.IAggregate;
import com.strawberry.app.common.cqengine.indexedstore.IndexedStoreBuilder;
import com.strawberry.app.common.cqengine.indexedstore.IndexedStoreImpl;
import java.util.List;
import java.util.stream.Collectors;

public class InternalStoreBuilder {

  public static List<IndexedStoreImpl> buildStateStores(List<IAggregate> aggregates) {
    return aggregates.stream()
        .map(aggregate -> (IndexedStoreImpl) IndexedStoreBuilder.builder()
            .name(aggregate.stateClass().getName())
            .kClass(aggregate.identityClass())
            .vClass(aggregate.stateClass())
            .indices(aggregate.indices())
            .identityGetter(aggregate.identityGetter())
            .build()
            .build())
        .collect(Collectors.toList());
  }
}
