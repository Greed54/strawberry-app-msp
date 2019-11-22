package com.strawberry.app.core.context.employee.behavior;

import com.strawberry.app.core.context.common.behavior.Behavior;
import com.strawberry.app.core.context.employee.StrawberryEmployee;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeNoteCommand;
import com.strawberry.app.core.context.employee.command.StrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAmendedNoteEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeEvent;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeId;
import com.strawberry.app.core.context.employee.utils.StrawberryEmployeeEventBuilderUtils;
import com.strawberry.app.core.context.validation.ValidationHelper;
import java.util.Collection;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AmendStrawberryEmployeeNoteBehavior implements
    Behavior<StrawberryEmployeeId, StrawberryEmployeeEvent, StrawberryEmployeeCommand, StrawberryEmployee> {

  @Override
  public Collection<StrawberryEmployeeEvent> commandToEvents(StrawberryEmployeeCommand command, Optional<StrawberryEmployee> state) {
    return new ValidationHelper<StrawberryEmployeeId, StrawberryEmployee, StrawberryEmployeeEvent, AmendStrawberryEmployeeNoteCommand>(
        state, (AmendStrawberryEmployeeNoteCommand) command)
        .present(StrawberryEmployee.class)
        .success((amendStrawberryEmployeeNoteCommand, strawberryEmployee) ->
            StrawberryEmployeeAmendedNoteEvent.builder()
                .from((HasStrawberryEmployeeId) amendStrawberryEmployeeNoteCommand)
                .build())
        .failed(StrawberryEmployeeEventBuilderUtils::buildFailedEvent);
  }

  @Override
  public StrawberryEmployee eventToState(StrawberryEmployeeEvent event, Optional<StrawberryEmployee> current) {
    StrawberryEmployeeAmendedNoteEvent amendedNoteEvent = (StrawberryEmployeeAmendedNoteEvent) event;
    return  StrawberryEmployee.builder()
        .from(current.get())
        .from(event)
        .modifiedAt(amendedNoteEvent.modifiedAt())
        .modifiedBy(amendedNoteEvent.modifiedBy())
        .build();
  }
}
