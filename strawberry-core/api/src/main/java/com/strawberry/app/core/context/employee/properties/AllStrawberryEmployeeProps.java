package com.strawberry.app.core.context.employee.properties;

import com.strawberry.app.core.context.common.property.context.identity.card.HasStrawberryCardId;

public interface AllStrawberryEmployeeProps extends HasStrawberryCardId,
    HasStrawberryEmployeeFirstName,
    HasStrawberryEmployeeLastName,
    HasStrawberryEmployeeRole, //TODO: teamID
    HasStrawberryEmployeeNote {

}
