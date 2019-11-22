package com.strawberry.app.core.context.team;

import com.strawberry.app.core.context.common.property.context.HasRemoved;
import com.strawberry.app.core.context.common.property.context.created.HasCreatedAt;
import com.strawberry.app.core.context.common.property.context.created.HasOptionalCreatedBy;
import com.strawberry.app.core.context.common.property.context.modified.HasOptionalModified;
import com.strawberry.app.core.context.cqrscommon.projection.State;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.team.properties.BaseStrawberryTeamProps;
import com.strawberry.app.core.context.team.properties.HasStrawberryTeamId;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryTeam extends State<StrawberryTeamId>, HasStrawberryTeamId, BaseStrawberryTeamProps, HasRemoved, HasCreatedAt,
    HasOptionalCreatedBy, HasOptionalModified {

}
