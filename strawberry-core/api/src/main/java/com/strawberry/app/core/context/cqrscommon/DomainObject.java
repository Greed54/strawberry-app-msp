package com.strawberry.app.core.context.cqrscommon;

public interface DomainObject<K extends Identity<?>> {

  K identity();
}
