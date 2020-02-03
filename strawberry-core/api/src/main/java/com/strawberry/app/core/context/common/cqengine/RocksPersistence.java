package com.strawberry.app.core.context.common.cqengine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.persistence.Persistence;
import com.googlecode.cqengine.query.option.QueryOptions;
import java.util.function.Function;

public class RocksPersistence<K, V> implements Persistence<V, String> {

  RocksObjectStore<K, V> objectStore;

  public RocksPersistence(String name, Function<? super V, K> identityGetter, Class<K> kClass, Class<V> vClass) {
    this.objectStore = new RocksObjectStore<>(name, identityGetter, kClass, vClass);
  }

  public void init(String stateDir, ObjectMapper objectMapper) {
    objectStore.init(stateDir, objectMapper);
  }

  @Override
  public RocksObjectStore<K, V> createObjectStore() {
    return objectStore;
  }

  @Override
  public boolean supportsIndex(Index<V> index) {
    return false;
  }

  @Override
  public void openRequestScopeResources(QueryOptions queryOptions) {

  }

  @Override
  public void closeRequestScopeResources(QueryOptions queryOptions) {

  }

  @Override
  public SimpleAttribute<V, String> getPrimaryKeyAttribute() {
    return null;
  }
}
