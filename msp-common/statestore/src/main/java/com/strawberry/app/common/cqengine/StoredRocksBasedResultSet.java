package com.strawberry.app.common.cqengine;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.stored.StoredResultSet;
import com.strawberry.app.common.rocksdb.KeyValueStore;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StoredRocksBasedResultSet<K, V> extends StoredResultSet<V> {

  Set<K> keySet = Collections.newSetFromMap(new ConcurrentHashMap<>());
  KeyValueStore<K, V> keyValueStore;
  Function<? super V, K> identityGetter;

  @Override
  public boolean add(V o) {
    return keySet.add(identityGetter.apply(o));
  }

  @Override
  public boolean remove(V o) {
    return keySet.remove(identityGetter.apply(o));
  }

  @Override
  public void clear() {
    keySet.clear();
  }

  @NotNull
  @Override
  public Iterator<V> iterator() {
    return keySet.parallelStream()
        .map(keyValueStore::get)
        .iterator();
  }

  @Override
  public boolean contains(V object) {
    return keySet.contains(object);
  }

  @Override
  public boolean matches(V object) {
    return contains(object);
  }

  @Override
  public Query<V> getQuery() {
    throw new UnsupportedOperationException();
  }

  @Override
  public QueryOptions getQueryOptions() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getRetrievalCost() {
    return 0;
  }

  @Override
  public int getMergeCost() {
    return keySet.size();
  }

  @Override
  public int size() {
    return 0;
  }

  @Override
  public boolean isEmpty() {
    return keySet.isEmpty();
  }

  @Override
  public boolean isNotEmpty() {
    return !keySet.isEmpty();
  }

  @Override
  public void close() {

  }
}
