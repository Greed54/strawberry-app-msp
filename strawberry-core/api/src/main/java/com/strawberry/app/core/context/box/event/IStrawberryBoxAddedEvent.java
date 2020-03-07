package com.strawberry.app.core.context.box.event;

import com.strawberry.app.common.property.context.created.HasCreatedAt;
import com.strawberry.app.core.context.box.properties.HasStrawberryBoxAmount;
import com.strawberry.app.core.context.box.properties.HasStrawberryBoxKilograms;
import com.strawberry.app.core.context.box.properties.HasStrawberryEmployeeId;
import com.strawberry.app.core.context.box.properties.HasStrawberryWeightId;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryBoxAddedEvent extends StrawberryBoxEvent,
    HasStrawberryEmployeeId,
    HasStrawberryBoxKilograms,
    HasStrawberryBoxAmount,
    HasStrawberryWeightId,
    HasCreatedAt {

}
