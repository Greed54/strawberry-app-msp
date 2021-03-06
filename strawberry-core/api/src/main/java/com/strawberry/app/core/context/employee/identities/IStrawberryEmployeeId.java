package com.strawberry.app.core.context.employee.identities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.strawberry.app.common.identity.StringIdentity;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonSerialize(as = StrawberryEmployeeId.class)
@JsonDeserialize(as = StrawberryEmployeeId.class)
public interface IStrawberryEmployeeId extends StringIdentity {

}
