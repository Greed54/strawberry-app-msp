package com.strawberry.app.core.context.employee.command;

import com.strawberry.app.common.property.context.created.HasCreated;
import com.strawberry.app.core.context.employee.properties.AllStrawberryEmployeeProps;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IAddStrawberryEmployeeCommand extends StrawberryEmployeeCommand, AllStrawberryEmployeeProps, HasCreated {

}
