package com.strawberry.app.common.context;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValidationSuccess implements ValidationResult {

  @Override
  public boolean isValid() {
    return true;
  }

}
