package com.strawberry.app.core.context.box.command;

import com.strawberry.app.common.property.context.created.HasCreatedAt;
import com.strawberry.app.core.context.box.properties.HasStrawberryBoxAmount;
import com.strawberry.app.core.context.box.properties.HasStrawberryBoxKilograms;
import com.strawberry.app.core.context.box.properties.HasStrawberryCardId;
import com.strawberry.app.core.context.box.properties.HasStrawberryWeightId;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IAddStrawberryBoxCommand extends StrawberryBoxCommand,
    HasStrawberryCardId,
    HasStrawberryBoxKilograms,
    HasStrawberryBoxAmount,
    HasStrawberryWeightId,
    HasCreatedAt {

}
