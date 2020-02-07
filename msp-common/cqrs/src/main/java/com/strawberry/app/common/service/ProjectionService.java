package com.strawberry.app.common.service;

import com.strawberry.app.common.Identity;
import com.strawberry.app.common.projection.Projection;

public interface ProjectionService<E extends Projection<I>, I extends Identity<?>> {

  E saveProjection(E projection);
}
