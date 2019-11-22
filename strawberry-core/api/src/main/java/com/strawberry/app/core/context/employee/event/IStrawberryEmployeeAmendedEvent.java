package com.strawberry.app.core.context.employee.event;

import com.strawberry.app.core.context.common.property.context.identity.card.HasStrawberryCardId;
import com.strawberry.app.core.context.common.property.context.modified.HasModified;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeFirstName;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeLastName;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeNote;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeTeamId;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryEmployeeAmendedEvent extends StrawberryEmployeeEvent,
    HasStrawberryCardId,
    HasStrawberryEmployeeFirstName,
    HasStrawberryEmployeeLastName,
    HasStrawberryEmployeeTeamId,
    HasStrawberryEmployeeNote,
    HasModified {

}
