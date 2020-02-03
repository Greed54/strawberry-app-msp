package com.strawberry.app.core.context.team.utils;

import static com.strawberry.app.core.context.validation.ValidationUtils.isPresent;

import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.service.StrawberryEmployeeService;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.team.service.StrawberryTeamService;
import com.strawberry.app.core.context.validation.context.ValidationResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class StrawberryTeamValidator {

  StrawberryTeamService strawberryTeamService;
  StrawberryEmployeeService strawberryEmployeeService;

  public ValidationResult validateTeam(StrawberryTeamId teamId) {
    return isPresent(strawberryTeamService::getActiveStrawberryTeam, teamId);
  }

  public ValidationResult validateEmployee(StrawberryEmployeeId employeeId) {
    return isPresent(strawberryEmployeeService::getActiveStrawberryEmployee, employeeId);
  }

}
