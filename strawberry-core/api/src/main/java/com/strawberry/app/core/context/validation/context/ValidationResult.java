package com.strawberry.app.core.context.validation.context;

public interface ValidationResult {

  boolean isValid();

  default String getMessage() {
    return "";
  }

}
