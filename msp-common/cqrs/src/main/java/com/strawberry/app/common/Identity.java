package com.strawberry.app.common;

public interface Identity<T> {

  T value();

  default Identity<T> aggregateId() {
    return this;
  }
}
