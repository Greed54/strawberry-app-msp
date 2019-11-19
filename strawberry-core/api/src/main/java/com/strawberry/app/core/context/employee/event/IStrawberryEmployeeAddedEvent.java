package com.strawberry.app.core.context.employee.event;

import com.strawberry.app.core.context.common.property.context.created.HasCreated;
import com.strawberry.app.core.context.employee.properties.AllStrawberryEmployeeProps;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryEmployeeAddedEvent extends StrawberryEmployeeEvent, AllStrawberryEmployeeProps, HasCreated {

}
