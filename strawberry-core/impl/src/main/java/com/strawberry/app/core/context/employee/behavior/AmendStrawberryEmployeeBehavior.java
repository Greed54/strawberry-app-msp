package com.strawberry.app.core.context.employee.behavior;

import com.strawberry.app.common.ValidationHelper;
import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.core.context.employee.StrawberryEmployee;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.StrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAmendedEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeEvent;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeId;
import com.strawberry.app.core.context.employee.utils.StrawberryEmployeeEventBuilderUtils;
import com.strawberry.app.core.context.employee.utils.StrawberryEmployeeValidator;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AmendStrawberryEmployeeBehavior implements
    Behavior<StrawberryEmployeeId, StrawberryEmployeeEvent, StrawberryEmployeeCommand, StrawberryEmployee> {

  StrawberryEmployeeValidator employeeValidator;

  public AmendStrawberryEmployeeBehavior(StrawberryEmployeeValidator employeeValidator) {
    this.employeeValidator = employeeValidator;
  }

  @Override
  public Collection<StrawberryEmployeeEvent> commandToEvents(StrawberryEmployeeCommand command, Optional<StrawberryEmployee> state) {
    return new ValidationHelper<StrawberryEmployeeId, StrawberryEmployee, StrawberryEmployeeEvent, AmendStrawberryEmployeeCommand>(
        state, (AmendStrawberryEmployeeCommand) command)
        .present(StrawberryEmployee.class)
        .validate((amendStrawberryEmployeeCommand, strawberryEmployee) -> employeeValidator.validateTeam(amendStrawberryEmployeeCommand.teamId()))
        .validate((amendStrawberryEmployeeCommand, strawberryEmployee) -> employeeValidator
            .validateCardIdIsExist(amendStrawberryEmployeeCommand.cardId(), strawberryEmployee.identity()))
        .success((amendStrawberryEmployeeCommand, strawberryEmployee) ->
            StrawberryEmployeeAmendedEvent.builder()
                .from((HasStrawberryEmployeeId) amendStrawberryEmployeeCommand)
                .build())
        .failed(StrawberryEmployeeEventBuilderUtils::buildFailedEvent);
  }

  @Override
  public StrawberryEmployee eventToState(StrawberryEmployeeEvent event, Optional<StrawberryEmployee> current) {
    StrawberryEmployeeAmendedEvent employeeAmendedEvent = (StrawberryEmployeeAmendedEvent) event;
    return StrawberryEmployee.builder()
        .from(current.get())
        .from(event)
        .modifiedAt(employeeAmendedEvent.modifiedAt())
        .modifiedBy(employeeAmendedEvent.modifiedBy())
        .build();
  }

  @Override
  public Collection<Class<? extends StrawberryEmployeeCommand>> getSupportedCommands() {
    return Collections.singletonList(AmendStrawberryEmployeeCommand.class);
  }

  @Override
  public Collection<Class<? extends StrawberryEmployeeEvent>> getSupportedEvents() {
    return Collections.singletonList(StrawberryEmployeeAmendedEvent.class);
  }

  @Override
  public Class<? extends StrawberryEmployee> getSupportedState() {
    return StrawberryEmployee.class;
  }
}
