package com.strawberry.app.core.context.cqrscommon.service;

import com.strawberry.app.core.context.cqrscommon.Identity;
import com.strawberry.app.core.context.cqrscommon.projection.Projection;

public interface ProjectionService<E extends Projection<I>, I extends Identity<?>> {

  E saveProjection(E projection);
}
