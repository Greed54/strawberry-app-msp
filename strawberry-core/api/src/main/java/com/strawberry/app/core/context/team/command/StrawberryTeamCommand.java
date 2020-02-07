package com.strawberry.app.core.context.team.command;

import com.strawberry.app.common.Command;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.team.properties.HasStrawberryTeamId;

public interface StrawberryTeamCommand extends Command<StrawberryTeamId>, HasStrawberryTeamId {

}
