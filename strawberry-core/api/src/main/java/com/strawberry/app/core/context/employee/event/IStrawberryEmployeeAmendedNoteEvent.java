package com.strawberry.app.core.context.employee.event;

import com.strawberry.app.core.context.common.property.context.modified.HasModified;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeNote;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryEmployeeAmendedNoteEvent extends StrawberryEmployeeEvent, HasStrawberryEmployeeNote, HasModified {

}
