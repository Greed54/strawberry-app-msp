package com.strawberry.app.core.context.person.properties;

import org.immutables.value.Value.Default;

public interface HasStrawberryPersonIsAdmin {

  @Default
  default Boolean isAdmin() {
    return false;
  }
}
