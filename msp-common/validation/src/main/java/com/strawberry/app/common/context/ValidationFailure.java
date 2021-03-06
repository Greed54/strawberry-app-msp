package com.strawberry.app.common.context;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public class ValidationFailure implements ValidationResult {

  private String message;

  @Override
  public boolean isValid() {
    return false;
  }

  @Override
  public String getMessage() {
    return message;
  }

}
