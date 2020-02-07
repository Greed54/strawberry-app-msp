package com.strawberry.app.core.context.team.event;

import com.strawberry.app.common.property.context.modified.HasModified;
import com.strawberry.app.core.context.team.properties.HasStrawberryTeamLeadId;
import com.strawberry.app.core.context.team.properties.HasStrawberryTeamName;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryTeamAmendedEvent extends StrawberryTeamEvent, HasStrawberryTeamName, HasStrawberryTeamLeadId, HasModified {

}
