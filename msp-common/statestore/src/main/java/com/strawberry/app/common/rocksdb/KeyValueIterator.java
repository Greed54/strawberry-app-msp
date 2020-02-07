package com.strawberry.app.common.rocksdb;

import java.io.Closeable;
import java.util.Iterator;

public interface KeyValueIterator<K, V> extends Iterator<KeyValue<K, V>>, Closeable {

  @Override
  void close();

  K peekNextKey();
}
