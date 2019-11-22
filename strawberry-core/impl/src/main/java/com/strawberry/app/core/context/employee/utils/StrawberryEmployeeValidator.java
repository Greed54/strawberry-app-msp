package com.strawberry.app.core.context.employee.utils;

import static com.strawberry.app.core.context.validation.ValidationUtils.isPresent;
import static com.strawberry.app.core.context.validation.ValidationUtils.validateCollectionEmpty;

import com.strawberry.app.core.context.common.property.context.identity.card.CardId;
import com.strawberry.app.core.context.employee.service.StrawberryEmployeeProjectionService;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.team.service.StrawberryTeamProjectionService;
import com.strawberry.app.core.context.validation.context.ValidationResult;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrawberryEmployeeValidator {

  StrawberryTeamProjectionService teamProjectionService;
  StrawberryEmployeeProjectionService employeeProjectionService;

  public StrawberryEmployeeValidator(StrawberryTeamProjectionService teamProjectionService,
      StrawberryEmployeeProjectionService employeeProjectionService) {
    this.teamProjectionService = teamProjectionService;
    this.employeeProjectionService = employeeProjectionService;
  }

  public ValidationResult validateTeam(StrawberryTeamId teamId) {
    return isPresent(teamProjectionService::getProjection, teamId);
  }

  public ValidationResult validateTeamLeadIsExist(StrawberryTeamId teamId) {
    return validateCollectionEmpty(employeeProjectionService.getTeamLeadEmployeeByTeam(teamId), "Team lead is exist or ");
  }

  public ValidationResult validateCardIdIsExist(CardId cardId) {
    return validateCollectionEmpty(employeeProjectionService.getEmployeesByCardId(cardId), "Card id is exist or ");
  }
}
