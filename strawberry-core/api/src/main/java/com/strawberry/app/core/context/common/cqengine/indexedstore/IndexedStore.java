package com.strawberry.app.core.context.common.cqengine.indexedstore;

import com.strawberry.app.core.context.common.rocksdb.KeyValueStore;

public interface IndexedStore<K, V> extends KeyValueStore<K, V>, ReadOnlyIndexedStore<K, V>{

}
