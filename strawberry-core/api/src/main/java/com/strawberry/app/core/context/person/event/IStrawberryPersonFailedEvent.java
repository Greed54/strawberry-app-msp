package com.strawberry.app.core.context.person.event;

import com.strawberry.app.common.utils.read.event.FailedEvent;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryPersonFailedEvent extends StrawberryPersonEvent, FailedEvent {

}
