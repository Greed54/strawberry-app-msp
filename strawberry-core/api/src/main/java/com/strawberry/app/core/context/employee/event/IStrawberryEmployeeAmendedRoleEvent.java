package com.strawberry.app.core.context.employee.event;

import com.strawberry.app.core.context.common.property.context.modified.HasModified;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeRole;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryEmployeeAmendedRoleEvent extends StrawberryEmployeeEvent, HasStrawberryEmployeeRole, HasModified {

}
