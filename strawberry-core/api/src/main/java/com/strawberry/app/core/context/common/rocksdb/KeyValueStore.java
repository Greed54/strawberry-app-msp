package com.strawberry.app.core.context.common.rocksdb;

import java.util.List;

public interface KeyValueStore<K, V> extends StateStore, ReadOnlyKeyValueStore<K, V> {

  void put(K key, V value);

  V putIfAbsent(K key, V value);

  void putAll(List<KeyValue<K, V>> entries);

  V delete(K key);
}
