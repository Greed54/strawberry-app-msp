package com.strawberry.app.core.context.employee.behavior;

import com.strawberry.app.core.context.common.behavior.Behavior;
import com.strawberry.app.core.context.employee.StrawberryEmployee;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeRoleCommand;
import com.strawberry.app.core.context.employee.command.StrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAmendedRoleEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeEvent;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeId;
import com.strawberry.app.core.context.employee.utils.StrawberryEmployeeEventBuilderUtils;
import com.strawberry.app.core.context.employee.utils.StrawberryEmployeeValidator;
import com.strawberry.app.core.context.enums.EmployeeRole;
import com.strawberry.app.core.context.validation.ValidationHelper;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AmendStrawberryEmployeeRoleBehavior implements
    Behavior<StrawberryEmployeeId, StrawberryEmployeeEvent, StrawberryEmployeeCommand, StrawberryEmployee> {

  StrawberryEmployeeValidator employeeValidator;

  public AmendStrawberryEmployeeRoleBehavior(StrawberryEmployeeValidator employeeValidator) {
    this.employeeValidator = employeeValidator;
  }

  @Override
  public Collection<StrawberryEmployeeEvent> commandToEvents(StrawberryEmployeeCommand command, Optional<StrawberryEmployee> state) {
    return new ValidationHelper<StrawberryEmployeeId, StrawberryEmployee, StrawberryEmployeeEvent, AmendStrawberryEmployeeRoleCommand>(
        state, (AmendStrawberryEmployeeRoleCommand) command)
        .present(StrawberryEmployee.class)
        .validateIf(amendStrawberryEmployeeRoleCommand -> Objects.equals(amendStrawberryEmployeeRoleCommand.employeeRole(), EmployeeRole.TEAM_LEAD),
            (addStrawberryEmployeeCommand, strawberryEmployee) -> employeeValidator.validateTeamLeadIsExist(strawberryEmployee.teamId()))
        .success((amendStrawberryEmployeeRoleCommand, strawberryEmployee) ->
            StrawberryEmployeeAmendedRoleEvent.builder()
                .from((HasStrawberryEmployeeId) amendStrawberryEmployeeRoleCommand)
                .build())
        .failed(StrawberryEmployeeEventBuilderUtils::buildFailedEvent);
  }

  @Override
  public StrawberryEmployee eventToState(StrawberryEmployeeEvent event, Optional<StrawberryEmployee> current) {
    StrawberryEmployeeAmendedRoleEvent amendedRoleEvent = (StrawberryEmployeeAmendedRoleEvent) event;
    return StrawberryEmployee.builder()
        .from(current.get())
        .from(event)
        .modifiedAt(amendedRoleEvent.modifiedAt())
        .modifiedBy(amendedRoleEvent.modifiedBy())
        .build();
  }

  @Override
  public Collection<Class<? extends StrawberryEmployeeCommand>> getSupportedCommands() {
    return Collections.singletonList(AmendStrawberryEmployeeRoleCommand.class);
  }

  @Override
  public Collection<Class<? extends StrawberryEmployeeEvent>> getSupportedEvents() {
    return Collections.singletonList(StrawberryEmployeeAmendedRoleEvent.class);
  }

  @Override
  public Class<? extends StrawberryEmployee> getSupportedState() {
    return StrawberryEmployee.class;
  }
}
