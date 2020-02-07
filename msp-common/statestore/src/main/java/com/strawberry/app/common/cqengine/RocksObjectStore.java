package com.strawberry.app.common.cqengine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.cqengine.index.support.CloseableIterator;
import com.googlecode.cqengine.persistence.support.ObjectStore;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.strawberry.app.common.rocksdb.Bytes;
import com.strawberry.app.common.rocksdb.KeyValue;
import com.strawberry.app.common.rocksdb.KeyValueIterator;
import com.strawberry.app.common.rocksdb.ReadOnlyKeyValueStore;
import com.strawberry.app.common.rocksdb.RocksDBStore;
import com.strawberry.app.common.rocksdb.StateStore;
import java.util.Collection;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class RocksObjectStore<K, V> implements ObjectStore<V>, ReadOnlyKeyValueStore<K, V>, StateStore {

  Logger LOGGER = LoggerFactory.getLogger(RocksObjectStore.class);

  final RocksDBStore database;
  final Function<? super V, K> identityGetter;
  StateSerdes<K, V> stateSerdes;
  final Class<K> kClass;
  final Class<V> vClass;

  public RocksObjectStore(String name, Function<? super V, K> identityGetter, Class<K> kClass, Class<V> vClass) {
    this.database = new RocksDBStore(name);
    this.identityGetter = identityGetter;
    this.kClass = kClass;
    this.vClass = vClass;
  }

  @Override
  public int size(QueryOptions queryOptions) {
    final long actualSize = approximateNumEntries();
    return (int) actualSize != actualSize ? Integer.MAX_VALUE : (int) actualSize;
  }

  @Override
  public boolean contains(Object o, QueryOptions queryOptions) {
    @SuppressWarnings("unchecked") final V value = (V) o;
    return database.get(Bytes.wrap(stateSerdes.rawKey(identityGetter.apply(value)))) != null;
  }

  @Override
  public CloseableIterator<V> iterator(QueryOptions queryOptions) {
    final KeyValueIterator<K, V> iterator = all();
    return new CloseableIterator<V>() {

      @Override
      public void close() {
        iterator.close();
      }

      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public V next() {
        return iterator.next().value;
      }
    };
  }

  @Override
  public boolean isEmpty(QueryOptions queryOptions) {
    return database.approximateNumEntries() == 0;
  }

  @Override
  public boolean add(V object, QueryOptions queryOptions) {
    database.put(Bytes.wrap(stateSerdes.rawKey(identityGetter.apply(object))), stateSerdes.rawValue(object));
    LOGGER.debug("Added new object: {} in store: {}", object, name());
    return true;
  }

  @Override
  public boolean remove(Object o, QueryOptions queryOptions) {
    @SuppressWarnings("unchecked") final V value = (V) o;
    return database.delete(Bytes.wrap(stateSerdes.rawKey(identityGetter.apply(value)))) != null;
  }

  @Override
  public boolean containsAll(Collection<?> entries, QueryOptions queryOptions) {
    return entries.stream().allMatch(o -> contains(o, queryOptions));
  }

  @Override
  public boolean addAll(Collection<? extends V> entries, QueryOptions queryOptions) {
    for (V entry : entries) {
      add(entry, queryOptions);
    }
    return true;
  }

  @Override
  public boolean retainAll(Collection<?> c, QueryOptions queryOptions) {
    throw new UnsupportedOperationException("operations is not supported");
  }

  @Override
  public boolean removeAll(Collection<?> entries, QueryOptions queryOptions) {
    for (Object entry : entries) {
      remove(entry, queryOptions);
      LOGGER.debug("Object: {} removed from store: {}", entry, name());
    }
    return true;
  }

  @Override
  public void clear(QueryOptions queryOptions) {
    throw new UnsupportedOperationException("Operation is not supported.");
  }

  @Override
  public V get(K key) {
    return stateSerdes.valueFrom(database.get(Bytes.wrap(stateSerdes.rawKey(key))), vClass);
  }

  @Override
  public KeyValueIterator<K, V> range(K from, K to) {
    return new WrappingKeyValueIterator(database.range(Bytes.wrap(stateSerdes.rawKey(from)), Bytes.wrap(stateSerdes.rawKey(to))));
  }

  @Override
  public KeyValueIterator<K, V> all() {
    return new WrappingKeyValueIterator(database.all());
  }

  @Override
  public long approximateNumEntries() {
    return database.approximateNumEntries();
  }

  @Override
  public String name() {
    return database.name();
  }

  @Override
  public void init(String stateDir, ObjectMapper objectMapper) {
    this.stateSerdes = new StateSerdes<>(objectMapper);
    database.init(stateDir, objectMapper);
  }

  @Override
  public void flush() {
    database.flush();
  }

  @Override
  public void close() {
    database.close();
  }

  @Override
  public boolean persistent() {
    return true;
  }

  @Override
  public boolean isOpen() {
    return database.isOpen();
  }

  @RequiredArgsConstructor
  private class WrappingKeyValueIterator implements KeyValueIterator<K, V> {

    final KeyValueIterator<Bytes, byte[]> iterator;

    @Override
    public void close() {
      iterator.close();
    }

    @Override
    public K peekNextKey() {
      return stateSerdes.keyFrom(iterator.peekNextKey().get(), kClass);
    }

    @Override
    public boolean hasNext() {
      return iterator.hasNext();
    }

    @Override
    public KeyValue<K, V> next() {
      final KeyValue<Bytes, byte[]> nextEntry = iterator.next();
      return KeyValue.pair(stateSerdes.keyFrom(nextEntry.key.get(), kClass), stateSerdes.valueFrom(nextEntry.value, vClass));
    }
  }
}
