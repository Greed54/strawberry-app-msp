package com.strawberry.app.core.context.common.cqengine;

import com.strawberry.app.core.context.common.cqengine.indexedstore.IndexedStoreImpl;
import com.strawberry.app.core.context.cqrscommon.Identity;
import com.strawberry.app.core.context.cqrscommon.projection.Projection;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RepositoryFactory {

  List<IndexedStoreImpl> indexedStores;

  public <K extends Identity<?>, P extends Projection<K>> IndexedStoreImpl<K, P> getStateStore(Class<P> pClass) {
    return indexedStores.stream()
        .filter(indexedStore -> indexedStore.name().equals(pClass.getName()))
        .findFirst()
        .get();
  }


}
