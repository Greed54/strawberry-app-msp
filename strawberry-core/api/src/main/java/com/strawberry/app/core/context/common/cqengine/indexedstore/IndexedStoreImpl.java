package com.strawberry.app.core.context.common.cqengine.indexedstore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import com.strawberry.app.core.context.common.cqengine.ProjectionIndex;
import com.strawberry.app.core.context.common.cqengine.RocksObjectStore;
import com.strawberry.app.core.context.common.cqengine.RocksPersistence;
import com.strawberry.app.core.context.common.cqengine.StoredRocksBasedResultSet;
import com.strawberry.app.core.context.common.rocksdb.KeyValue;
import com.strawberry.app.core.context.common.rocksdb.KeyValueIterator;
import com.strawberry.app.core.context.common.rocksdb.ReadOnlyKeyValueStore;
import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class IndexedStoreImpl<K, V> implements IndexedStore<K, V>, ReadOnlyKeyValueStore<K, V>, DisposableBean {

  Logger LOGGER = LoggerFactory.getLogger(IndexedStoreImpl.class);

  final String name;

  final Class<K> kClass;
  final Class<V> vClass;

  final ImmutableSet<ProjectionIndex<V>> indices;
  final List<Closeable> openedIterators = Collections.synchronizedList(Lists.newArrayList());

  RocksObjectStore<K, V> objectStore;
  IndexedCollection<V> collection;
  Function<? super V, K> identityGetter;

  public IndexedStoreImpl(String name, Class<K> kClass, Class<V> vClass, Set<ProjectionIndex<V>> indices, Function<? super V, K> identityGetter) {
    this.name = name;
    this.kClass = kClass;
    this.vClass = vClass;
    this.identityGetter = identityGetter;

    this.indices = ImmutableSet.copyOf(indices);
  }

  @Override
  public void init(String stateDir, ObjectMapper objectMapper) {
    // init persistence
    final RocksPersistence<K, V> persistence = new RocksPersistence<>(name, identityGetter, kClass, vClass);
    persistence.init(stateDir, objectMapper);

    // init cqEngine store
    objectStore = persistence.createObjectStore();
    collection = new ConcurrentIndexedCollection<>(persistence);

    initIndices();
    LOGGER.debug("Objects number in store {}: {}", name, objectStore.approximateNumEntries());
  }

  @Override
  public String name() {
    return this.name;
  }

  @Override
  public void flush() {

  }

  @Override
  public void close() {
    if (objectStore == null) {
      return;
    }

    List<Closeable> iterators;
    synchronized (openedIterators) {
      iterators = Lists.newArrayList(openedIterators);
    }

    iterators.forEach(closeable -> {
      try {
        closeable.close();
      } catch (IOException e) {
        throw new IllegalStateException(e.getMessage(), e);
      }
    });

    objectStore.close();
    objectStore = null;
    collection = null;
  }

  @Override
  public boolean persistent() {
    return true;
  }

  @Override
  public boolean isOpen() {
    return false;
  }

  @Override
  public synchronized ResultSet<V> retrieve(Query<V> query) {
    return retrieve(query, new QueryOptions());
  }

  @Override
  public ResultSet<V> retrieve(Query<V> query, QueryOptions options) {
    Objects.requireNonNull(query, "query is null");

    final ResultSet<V> resultSet = new ResultSetDelegate<V>(collection.retrieve(query, options));

    openedIterators.add(resultSet);

    return resultSet;
  }

  @Override
  public synchronized void put(K key, V value) {
    Objects.requireNonNull(key, "key is null");
    objectStore.add(value, null);

    final V projection = findByKey(key);
    updateInternal(projection, value);
  }

  @Override
  public synchronized V putIfAbsent(K key, V value) {
    Objects.requireNonNull(key, "key is null");
    validateKeyValuePair(key, value);

    final V projection = findByKey(key);
    if (projection == null) {
      updateInternal(projection, value);
    }
    return projection;
  }

  @Override
  public void putAll(List<KeyValue<K, V>> entries) {
    Objects.requireNonNull(entries, "entries is null");

    entries.forEach(entry -> {
      validateKeyValuePair(entry.key, entry.value);
      put(entry.key, entry.value);
    });
  }

  @Override
  public V delete(K key) {
    Objects.requireNonNull(key, "key is null");
    objectStore.remove(key, null);

    final V projection = findByKey(key);
    if (projection != null) {
      updateInternal(projection, null);
    }

    return projection;
  }

  @Override
  public V get(K key) {
    Objects.requireNonNull(key, "key is null");

    return findByKey(key);
  }

  @Override
  public KeyValueIterator<K, V> range(K from, K to) {
    return objectStore.range(from, to);
  }

  @Override
  public KeyValueIterator<K, V> all() {
    return objectStore.all();
  }

  @Override
  public long approximateNumEntries() {
    return collection.size();
  }

  private V findByKey(K key) {
    return objectStore.get(key);
  }

  private void updateInternal(V oldState, V newState) {
    collection.update(wrapProjection(oldState), wrapProjection(newState));
  }

  private Collection<V> wrapProjection(V projection) {
    return projection == null ? Collections.emptySet() : ImmutableSet.of(projection);
  }

  private void validateKeyValuePair(K key, V value) {
    if (value != null && !identityGetter.apply(value).equals(key)) {
      throw new IllegalStateException(String.format("value.identity(%s) != key(%s)", key, identityGetter.apply(value)));
    }
  }

  private void initIndices() {
    indices.stream()
        .map(projectionIndex -> projectionIndex.supply(() -> new StoredRocksBasedResultSet<>(this, identityGetter)))
        .forEach(collection::addIndex);
  }

  @Override
  public void destroy() {
    close();
  }

  @AllArgsConstructor
  class ResultSetDelegate<O> extends ResultSet<O> {

    private final ResultSet<O> resultSet;

    @Override
    public Iterator<O> iterator() {
      return resultSet.iterator();
    }

    @Override
    public boolean contains(O object) {
      return resultSet.contains(object);
    }

    @Override
    public boolean matches(O object) {
      return resultSet.matches(object);
    }

    @Override
    public Query<O> getQuery() {
      return resultSet.getQuery();
    }

    @Override
    public QueryOptions getQueryOptions() {
      return resultSet.getQueryOptions();
    }

    @Override
    public int getRetrievalCost() {
      return resultSet.getRetrievalCost();
    }

    @Override
    public int getMergeCost() {
      return resultSet.getMergeCost();
    }

    @Override
    public int size() {
      return resultSet.size();
    }

    @Override
    public void close() {
      resultSet.close();
      openedIterators.remove(this);
    }
  }
}
