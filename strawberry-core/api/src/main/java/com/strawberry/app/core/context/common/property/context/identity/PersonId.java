package com.strawberry.app.core.context.common.property.context.identity;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PersonId extends BaseStringId{

  public PersonId(String key) {
    super(key);
  }

  public static PersonId next() {
    return new PersonId(UUID.randomUUID().toString());
  }
}
