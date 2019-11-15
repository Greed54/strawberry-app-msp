package com.strawberry.app.core.context.employee.event;

import com.strawberry.app.core.context.cqrscommon.BusinessEvent;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeId;

public interface StrawberryEmployeeEvent extends BusinessEvent<StrawberryEmployeeId>, HasStrawberryEmployeeId {

}
