package com.strawberry.app.core.context.cqrscommon;

public interface Identity<T> {

  T value();

  default Identity<T> aggregateId() {
    return this;
  }
}
