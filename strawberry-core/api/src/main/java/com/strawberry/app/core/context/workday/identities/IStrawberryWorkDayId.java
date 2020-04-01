package com.strawberry.app.core.context.workday.identities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.strawberry.app.common.identity.StringIdentity;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonSerialize(as = StrawberryWorkDayId.class)
@JsonDeserialize(as = StrawberryWorkDayId.class)
public interface IStrawberryWorkDayId extends StringIdentity {

}
