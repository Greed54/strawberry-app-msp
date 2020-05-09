package com.strawberry.app.core.context.employee.service;

import com.googlecode.cqengine.query.QueryFactory;
import com.strawberry.app.common.property.context.identity.card.CardId;
import com.strawberry.app.core.context.employee.IStrawberryEmployee.Attributes;
import com.strawberry.app.core.context.employee.StrawberryEmployee;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.enums.EmployeeRole;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.utils.service.RepositoryService;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class StrawberryEmployeeService {

  RepositoryService repositoryService;

  public Optional<StrawberryEmployee> getEmployee(StrawberryEmployeeId employeeId) {
    return repositoryService.retrieveState(employeeId, StrawberryEmployee.class);
  }

  public Optional<StrawberryEmployee> getActiveStrawberryEmployee(StrawberryEmployeeId strawberryEmployeeId) {
    return getEmployee(strawberryEmployeeId)
        .filter(strawberryEmployee -> !strawberryEmployee.removed());
  }

  public List<StrawberryEmployee> getTeamLeadEmployeeByTeam(StrawberryTeamId teamId) {
    return repositoryService.retrieve(
        QueryFactory.and(
            QueryFactory.equal(Attributes.TEAM_ID, teamId),
            QueryFactory.equal(Attributes.EMPLOYEE_ROLE, EmployeeRole.TEAM_LEAD)
        ),
        StrawberryEmployee.class);
  }

  public List<StrawberryEmployee> getEmployeesByCardId(CardId cardId) {
    return repositoryService.retrieve(QueryFactory.equal(Attributes.CARD_ID, cardId), StrawberryEmployee.class);
  }

  public StrawberryEmployee getEmployeeByCardIdOrThrow(CardId cardId) {
    return repositoryService.retrieveUnique(QueryFactory.equal(Attributes.CARD_ID, cardId), StrawberryEmployee.class)
        .orElseThrow(() -> new IllegalStateException(String.format("StrawberryEmployee not present by card id %s", cardId)));
  }
}
