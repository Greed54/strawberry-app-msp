package com.strawberry.app.core.context.employee.behavior;

import com.strawberry.app.core.context.common.behavior.Behavior;
import com.strawberry.app.core.context.employee.StrawberryEmployee;
import com.strawberry.app.core.context.employee.command.AddStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.StrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAddedEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeEvent;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeId;
import com.strawberry.app.core.context.employee.utils.StrawberryEmployeeEventBuilderUtils;
import com.strawberry.app.core.context.employee.utils.StrawberryEmployeeValidator;
import com.strawberry.app.core.context.enums.EmployeeRole;
import com.strawberry.app.core.context.validation.ValidationHelper;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddStrawberryEmployeeBehavior implements
    Behavior<StrawberryEmployeeId, StrawberryEmployeeEvent, StrawberryEmployeeCommand, StrawberryEmployee> {

  StrawberryEmployeeValidator employeeValidator;

  public AddStrawberryEmployeeBehavior(StrawberryEmployeeValidator employeeValidator) {
    this.employeeValidator = employeeValidator;
  }

  @Override
  public Collection<StrawberryEmployeeEvent> commandToEvents(StrawberryEmployeeCommand command, Optional<StrawberryEmployee> state) {
    return new ValidationHelper<StrawberryEmployeeId, StrawberryEmployee, StrawberryEmployeeEvent, AddStrawberryEmployeeCommand>(state,
        (AddStrawberryEmployeeCommand) command)
        .validate((addStrawberryEmployeeCommand, strawberryEmployee) -> employeeValidator.validateTeam(addStrawberryEmployeeCommand.teamId()))
        .validateIf(addStrawberryEmployeeCommand -> Objects.equals(addStrawberryEmployeeCommand.employeeRole(), EmployeeRole.TEAM_LEAD),
            (addStrawberryEmployeeCommand, strawberryEmployee) -> employeeValidator.validateTeamLeadIsExist(addStrawberryEmployeeCommand.teamId()))
        .validate(
            (addStrawberryEmployeeCommand, strawberryEmployee) -> employeeValidator.validateCardIdIsExist(addStrawberryEmployeeCommand.cardId()))
        .success((addStrawberryEmployeeCommand, strawberryEmployee) ->
            StrawberryEmployeeAddedEvent.builder()
                .from((HasStrawberryEmployeeId) addStrawberryEmployeeCommand)
                .build())
        .failed(StrawberryEmployeeEventBuilderUtils::buildFailedEvent);
  }

  @Override
  public StrawberryEmployee eventToState(StrawberryEmployeeEvent event, Optional<StrawberryEmployee> current) {
    return StrawberryEmployee.builder()
        .from(event)
        .build();
  }
}
