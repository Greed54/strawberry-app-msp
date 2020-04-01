package com.strawberry.app.core.context.workday.event;

import com.strawberry.app.common.utils.read.event.FailedEvent;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryWorkDayFailedEvent extends StrawberryWorkDayEvent, FailedEvent {

}
