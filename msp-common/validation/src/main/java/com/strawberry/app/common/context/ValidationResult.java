package com.strawberry.app.common.context;

public interface ValidationResult {

  boolean isValid();

  default String getMessage() {
    return "";
  }

}
