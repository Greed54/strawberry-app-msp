package com.strawberry.app.core.context.person.identities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.strawberry.app.common.identity.StringIdentity;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonSerialize(as = StrawberryPersonId.class)
@JsonDeserialize(as = StrawberryPersonId.class)
public interface IStrawberryPersonId extends StringIdentity {

}
