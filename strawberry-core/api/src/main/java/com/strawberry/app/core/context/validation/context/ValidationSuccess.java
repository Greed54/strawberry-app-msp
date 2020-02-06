package com.strawberry.app.core.context.validation.context;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValidationSuccess implements ValidationResult {

  @Override
  public boolean isValid() {
    return true;
  }

}
