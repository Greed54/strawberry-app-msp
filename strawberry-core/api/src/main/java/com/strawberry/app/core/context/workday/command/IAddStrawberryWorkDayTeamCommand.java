package com.strawberry.app.core.context.workday.command;

import com.strawberry.app.common.property.context.modified.HasModified;
import com.strawberry.app.core.context.workday.properties.HasStrawberryWorkDayTeam;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IAddStrawberryWorkDayTeamCommand extends StrawberryWorkDayCommand, HasStrawberryWorkDayTeam, HasModified {

}
