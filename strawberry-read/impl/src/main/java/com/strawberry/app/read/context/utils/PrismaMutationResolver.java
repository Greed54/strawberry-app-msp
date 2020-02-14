package com.strawberry.app.read.context.utils;

import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Operation.Data;
import com.apollographql.apollo.api.Operation.Variables;
import com.strawberry.app.common.Identity;
import com.strawberry.app.common.projection.ProjectionEvent;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrismaMutationResolver {

  RepositoryService repositoryService;

  Logger LOGGER = LoggerFactory.getLogger(PrismaMutationResolver.class);

  public <K extends Identity<?>, PE extends ProjectionEvent<K>,
      CM extends Mutation<? extends Data, ? extends Optional<? extends Data>, ? extends Variables>,
      UM extends Mutation<? extends Data, ? extends Optional<? extends Data>, ? extends Variables>>
  Mutation resolveMutation(PE projectionEvent, CM createMutation, UM updateMutation) {
    Optional<PE> retrievedProjection = repositoryService.retrieve(projectionEvent.identity(), projectionEvent.getClass());

    if (retrievedProjection.isPresent()) {
      LOGGER.debug("Projection {} with (identity={}) was found, resolved update mutation", projectionEvent.getClass().getSimpleName(),
          projectionEvent.identity());

      return updateMutation;
    } else {
      repositoryService.saveProjection(projectionEvent.identity(), projectionEvent);

      LOGGER.debug("Projection {} with (identity={}) was not found, resolved create mutation", projectionEvent.getClass().getSimpleName(),
          projectionEvent.identity());

      return createMutation;
    }
  }
}
