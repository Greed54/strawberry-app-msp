package com.strawberry.app.core.context.team.command;

import com.strawberry.app.core.context.common.property.context.modified.HasModified;
import com.strawberry.app.core.context.team.properties.HasStrawberryTeamLeadId;
import com.strawberry.app.core.context.team.properties.HasStrawberryTeamName;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IAmendStrawberryTeamCommand extends StrawberryTeamCommand, HasStrawberryTeamName, HasStrawberryTeamLeadId, HasModified {

}
