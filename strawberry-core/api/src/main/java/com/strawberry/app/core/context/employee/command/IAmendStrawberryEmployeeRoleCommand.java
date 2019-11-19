package com.strawberry.app.core.context.employee.command;

import com.strawberry.app.core.context.common.property.context.modified.HasModified;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeRole;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IAmendStrawberryEmployeeRoleCommand extends StrawberryEmployeeCommand, HasStrawberryEmployeeRole, HasModified {

}
