package com.strawberry.app.common;

public interface Stream<K, V> {

  Class<K> getKeyClass();

  Class<V> getValueClass();
}
