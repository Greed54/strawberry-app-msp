package com.strawberry.app.common.property.context.identity.card;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.strawberry.app.common.property.context.identity.BaseStringId;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonSerialize
@JsonDeserialize
public class CardId extends BaseStringId {

  @JsonCreator(mode = Mode.PROPERTIES)
  public CardId(@JsonProperty("key") String key) {
    super(key);
  }
}
