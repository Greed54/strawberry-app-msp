package com.strawberry.app.core.context.box.event;

import com.strawberry.app.common.utils.read.event.FailedEvent;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryBoxFailedEvent extends StrawberryBoxEvent, FailedEvent {

}
