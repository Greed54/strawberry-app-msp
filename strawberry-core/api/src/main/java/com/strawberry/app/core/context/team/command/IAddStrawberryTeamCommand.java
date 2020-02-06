package com.strawberry.app.core.context.team.command;

import com.strawberry.app.core.context.common.property.context.created.HasCreated;
import com.strawberry.app.core.context.team.properties.BaseStrawberryTeamProps;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IAddStrawberryTeamCommand extends StrawberryTeamCommand, BaseStrawberryTeamProps, HasCreated {

}
