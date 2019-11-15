package com.strawberry.app.core.context.employee.event;

import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeName;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryEmployeeAddedEvent extends StrawberryEmployeeEvent, HasStrawberryEmployeeName {

}
