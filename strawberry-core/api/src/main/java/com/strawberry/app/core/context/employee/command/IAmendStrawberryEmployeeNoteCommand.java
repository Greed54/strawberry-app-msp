package com.strawberry.app.core.context.employee.command;

import com.strawberry.app.core.context.common.property.context.modified.HasModified;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeNote;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IAmendStrawberryEmployeeNoteCommand extends StrawberryEmployeeCommand, HasStrawberryEmployeeNote, HasModified {

}
