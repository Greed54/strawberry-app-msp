package com.strawberry.app.core.context.cqrscommon.service;

import com.strawberry.app.core.context.cqrscommon.Identity;
import com.strawberry.app.core.context.cqrscommon.ProjectionEntity;
import java.util.Optional;

public interface ProjectionService<E extends ProjectionEntity, I extends Identity<?>> {

  Optional<E> getProjection(I identity);

  E saveProjection(E projection);
}
