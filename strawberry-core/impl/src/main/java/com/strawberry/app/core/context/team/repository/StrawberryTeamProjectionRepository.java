package com.strawberry.app.core.context.team.repository;

import com.strawberry.app.core.context.team.projection.StrawberryTeamProjectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StrawberryTeamProjectionRepository extends JpaRepository<StrawberryTeamProjectionEntity, String> {

}
