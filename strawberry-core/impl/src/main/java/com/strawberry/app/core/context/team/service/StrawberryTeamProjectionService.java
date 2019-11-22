package com.strawberry.app.core.context.team.service;

import com.strawberry.app.core.context.cqrscommon.service.ProjectionService;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.team.projection.StrawberryTeamProjectionEntity;
import com.strawberry.app.core.context.team.repository.StrawberryTeamProjectionRepository;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrawberryTeamProjectionService implements ProjectionService<StrawberryTeamProjectionEntity, StrawberryTeamId> {

  StrawberryTeamProjectionRepository repository;

  public StrawberryTeamProjectionService(StrawberryTeamProjectionRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<StrawberryTeamProjectionEntity> getProjection(StrawberryTeamId identity) {
    return repository.findById(identity.value());
  }

  @Override
  public StrawberryTeamProjectionEntity saveProjection(StrawberryTeamProjectionEntity projection) {
    return repository.save(projection);
  }
}
