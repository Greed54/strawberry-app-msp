package com.strawberry.app.core.context.box.event;

import com.strawberry.app.common.property.context.modified.HasModified;
import com.strawberry.app.core.context.box.properties.HasOptionalStrawberryWorkDayId;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryBoxWorkDayAmendedEvent extends StrawberryBoxEvent,
    HasOptionalStrawberryWorkDayId,
    HasModified {

}
