package com.strawberry.app.common;

public interface DomainObject<K extends Identity<?>> {

  K identity();
}
