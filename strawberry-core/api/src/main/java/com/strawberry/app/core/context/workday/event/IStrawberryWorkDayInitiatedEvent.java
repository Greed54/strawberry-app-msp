package com.strawberry.app.core.context.workday.event;

import com.strawberry.app.common.property.context.created.HasCreatedAt;
import com.strawberry.app.core.context.workday.properties.AllStrawberryWorkDayProps;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryWorkDayInitiatedEvent extends StrawberryWorkDayEvent, AllStrawberryWorkDayProps, HasCreatedAt {

}
