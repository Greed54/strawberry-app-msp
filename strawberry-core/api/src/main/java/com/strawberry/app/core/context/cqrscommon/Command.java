package com.strawberry.app.core.context.cqrscommon;

import java.util.Optional;

public interface Command<K extends Identity<?>> extends DomainObject<K> {

  default Optional<Command<K>> next() {
    return Optional.empty();
  }
}
