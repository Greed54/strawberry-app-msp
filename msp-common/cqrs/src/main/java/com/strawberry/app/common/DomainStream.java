package com.strawberry.app.common;

public interface DomainStream<K extends Identity<?>, V extends DomainObject<K>> extends Stream<K, V> {

}
