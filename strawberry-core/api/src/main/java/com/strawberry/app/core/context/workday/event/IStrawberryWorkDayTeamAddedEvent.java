package com.strawberry.app.core.context.workday.event;

import com.strawberry.app.common.property.context.modified.HasModified;
import com.strawberry.app.core.context.workday.properties.HasStrawberryWorkDayTeam;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryWorkDayTeamAddedEvent extends StrawberryWorkDayEvent, HasStrawberryWorkDayTeam, HasModified {

}
