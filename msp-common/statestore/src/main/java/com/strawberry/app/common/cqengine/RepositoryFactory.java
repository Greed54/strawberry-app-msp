package com.strawberry.app.common.cqengine;

import com.strawberry.app.common.Identity;
import com.strawberry.app.common.cqengine.indexedstore.IndexedStoreImpl;
import com.strawberry.app.common.projection.Projection;
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
