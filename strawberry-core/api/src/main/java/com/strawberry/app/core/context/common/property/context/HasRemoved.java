package com.strawberry.app.core.context.common.property.context;

import org.immutables.value.Value;

public interface HasRemoved {

  @Value.Default
  default Boolean removed() {
    return false;
  }

}
