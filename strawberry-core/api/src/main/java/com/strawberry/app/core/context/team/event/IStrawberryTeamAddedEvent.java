package com.strawberry.app.core.context.team.event;

import com.strawberry.app.common.property.context.created.HasCreated;
import com.strawberry.app.core.context.team.properties.BaseStrawberryTeamProps;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryTeamAddedEvent extends StrawberryTeamEvent, BaseStrawberryTeamProps, HasCreated {

}
