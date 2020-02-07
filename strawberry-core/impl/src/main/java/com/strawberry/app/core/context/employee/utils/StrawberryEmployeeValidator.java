package com.strawberry.app.core.context.employee.utils;

import static com.strawberry.app.common.ValidationUtils.isPresent;
import static com.strawberry.app.common.ValidationUtils.validateCollectionEmpty;

import com.strawberry.app.common.context.ValidationResult;
import com.strawberry.app.common.property.context.identity.card.CardId;
import com.strawberry.app.core.context.employee.service.StrawberryEmployeeService;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.team.service.StrawberryTeamService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class StrawberryEmployeeValidator {

  StrawberryTeamService strawberryTeamService;
  StrawberryEmployeeService strawberryEmployeeService;

  public ValidationResult validateTeam(StrawberryTeamId teamId) {
    return isPresent(strawberryTeamService::getActiveStrawberryTeam, teamId);
  }

  public ValidationResult validateTeamLeadIsExist(StrawberryTeamId teamId) {
    return validateCollectionEmpty(strawberryEmployeeService.getTeamLeadEmployeeByTeam(teamId), "Team lead is exist or ");
  }

  public ValidationResult validateCardIdIsExist(CardId cardId) {
    return validateCollectionEmpty(strawberryEmployeeService.getEmployeesByCardId(cardId), "Card id is exist or ");
  }
}
