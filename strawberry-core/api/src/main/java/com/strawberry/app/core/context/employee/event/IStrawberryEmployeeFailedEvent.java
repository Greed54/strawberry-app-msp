package com.strawberry.app.core.context.employee.event;

import com.strawberry.app.core.context.common.utils.read.event.FailedEvent;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryEmployeeFailedEvent extends StrawberryEmployeeEvent, FailedEvent {

}
