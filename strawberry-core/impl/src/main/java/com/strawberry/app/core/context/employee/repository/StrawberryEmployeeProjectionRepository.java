package com.strawberry.app.core.context.employee.repository;

import com.strawberry.app.core.context.employee.projection.StrawberryEmployeeProjectionEntity;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StrawberryEmployeeProjectionRepository extends JpaRepository<StrawberryEmployeeProjectionEntity, String> {

  List<StrawberryEmployeeProjectionEntity> findAllByTeamIdAndEmployeeRole(String teamId, String employeeRole);

  List<StrawberryEmployeeProjectionEntity> findAllByCardId(String cardId);
}
