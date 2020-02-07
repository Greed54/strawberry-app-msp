package com.strawberry.app.common.property.context.identity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.strawberry.app.common.Identity;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
@JsonSerialize
public class BaseStringId implements Identity<String> {

  protected String value;

  public BaseStringId(String value) {
    this.value = value;
  }

  @Override
  public String value() {
    return value;
  }
}
