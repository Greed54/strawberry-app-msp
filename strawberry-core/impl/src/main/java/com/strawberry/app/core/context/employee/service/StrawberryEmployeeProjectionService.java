package com.strawberry.app.core.context.employee.service;

import com.strawberry.app.core.context.common.property.context.identity.card.CardId;
import com.strawberry.app.core.context.cqrscommon.service.ProjectionService;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.projection.StrawberryEmployeeProjectionEntity;
import com.strawberry.app.core.context.employee.repository.StrawberryEmployeeProjectionRepository;
import com.strawberry.app.core.context.enums.EmployeeRole;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrawberryEmployeeProjectionService implements ProjectionService<StrawberryEmployeeProjectionEntity, StrawberryEmployeeId> {

  StrawberryEmployeeProjectionRepository repository;

  public StrawberryEmployeeProjectionService(StrawberryEmployeeProjectionRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<StrawberryEmployeeProjectionEntity> getProjection(StrawberryEmployeeId identity) {
    return repository.findById(identity.value());
  }

  @Override
  public StrawberryEmployeeProjectionEntity saveProjection(StrawberryEmployeeProjectionEntity projection) {
    return repository.save(projection);
  }

  public List<StrawberryEmployeeProjectionEntity> getTeamLeadEmployeeByTeam(StrawberryTeamId teamId) {
    return repository.findAllByTeamIdAndEmployeeRole(teamId.value(), EmployeeRole.TEAM_LEAD.name());
  }

  public List<StrawberryEmployeeProjectionEntity> getEmployeesByCardId(CardId cardId) {
    return repository.findAllByCardId(cardId.value());
  }
}
