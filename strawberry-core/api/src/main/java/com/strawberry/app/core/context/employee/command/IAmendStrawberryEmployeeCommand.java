package com.strawberry.app.core.context.employee.command;

import com.strawberry.app.common.property.context.identity.card.HasStrawberryCardId;
import com.strawberry.app.common.property.context.modified.HasModified;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeFirstName;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeLastName;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeNote;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeTeamId;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IAmendStrawberryEmployeeCommand extends StrawberryEmployeeCommand,
    HasStrawberryCardId,
    HasStrawberryEmployeeFirstName,
    HasStrawberryEmployeeLastName,
    HasStrawberryEmployeeTeamId,
    HasStrawberryEmployeeNote,
    HasModified {

}
