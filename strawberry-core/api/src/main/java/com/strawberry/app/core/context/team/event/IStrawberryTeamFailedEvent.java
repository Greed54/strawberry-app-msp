package com.strawberry.app.core.context.team.event;

import com.strawberry.app.core.context.common.utils.read.event.FailedEvent;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryTeamFailedEvent extends StrawberryTeamEvent, FailedEvent {

}
