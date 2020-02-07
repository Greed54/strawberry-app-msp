package com.strawberry.app.common;

import java.util.Optional;

public interface Command<K extends Identity<?>> extends DomainObject<K> {

  default Optional<Command<K>> next() {
    return Optional.empty();
  }
}
