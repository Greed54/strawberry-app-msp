package com.strawberry.app.core.context.employee.command;

import com.strawberry.app.core.context.cqrscommon.Command;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeId;

public interface StrawberryEmployeeCommand extends Command<StrawberryEmployeeId>, HasStrawberryEmployeeId {

}
