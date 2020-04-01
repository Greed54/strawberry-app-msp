package com.strawberry.app.core.context.workday.event;

import com.strawberry.app.common.property.context.modified.HasModified;
import com.strawberry.app.core.context.workday.properties.HasStrawberryWorkDayPricePerKilo;
import com.strawberry.app.core.context.workday.properties.HasStrawberryWorkDayTareWeight;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryWorkDayAmendedEvent extends StrawberryWorkDayEvent,
    HasStrawberryWorkDayPricePerKilo,
    HasStrawberryWorkDayTareWeight,
    HasModified {

}
