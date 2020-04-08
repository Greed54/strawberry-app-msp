package com.strawberry.app.core.context.box.identities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.strawberry.app.common.identity.StringIdentity;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonSerialize(as = StrawberryBoxId.class)
@JsonDeserialize(as = StrawberryBoxId.class)
public interface IStrawberryBoxId extends StringIdentity {

}
