package com.strawberry.app.core.context.team.event;

import com.strawberry.app.core.context.cqrscommon.event.BusinessEvent;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.team.properties.HasStrawberryTeamId;

public interface StrawberryTeamEvent extends BusinessEvent<StrawberryTeamId>, HasStrawberryTeamId {

}
