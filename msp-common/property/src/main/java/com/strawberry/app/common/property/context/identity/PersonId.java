package com.strawberry.app.common.property.context.identity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonSerialize
@JsonDeserialize
public class PersonId extends BaseStringId {

  @JsonCreator(mode = Mode.PROPERTIES)
  public PersonId(@JsonProperty("key") String key) {
    super(key);
  }

  public static PersonId next() {
    return new PersonId(UUID.randomUUID().toString());
  }
}
