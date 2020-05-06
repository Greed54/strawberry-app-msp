package com.strawberry.app.common.cqengine.indexedstore;

import com.strawberry.app.common.rocksdb.KeyValueStore;

public interface IndexedStore<K, V> extends KeyValueStore<K, V>, ReadOnlyIndexedStore<K, V> {

}
