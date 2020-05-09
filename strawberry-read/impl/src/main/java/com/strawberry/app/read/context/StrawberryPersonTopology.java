package com.strawberry.app.read.context;

import static com.strawberry.app.read.context.utils.TopologyNames.PERSON_TOPOLOGY_NAME;

import com.apollographql.apollo.api.Mutation;
import com.google.common.collect.ImmutableSet;
import com.stawberry.app.read.prisma.graphql.CreateSPersonMutation;
import com.stawberry.app.read.prisma.graphql.type.SPersonCreateInput;
import com.strawberry.app.common.cqengine.ProjectionIndex;
import com.strawberry.app.common.projection.ProjectionEventStream;
import com.strawberry.app.common.topology.AbstractTopology;
import com.strawberry.app.core.context.person.identities.StrawberryPersonId;
import com.strawberry.app.core.context.person.projection.IStrawberryPersonProjectionEvent;
import com.strawberry.app.core.context.person.projection.StrawberryPersonProjectionEvent;
import com.strawberry.app.read.apollo.PrismaClient;
import com.strawberry.app.read.context.utils.PrismaMutationResolver;
import cool.graph.cuid.Cuid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ProcessingGroup(PERSON_TOPOLOGY_NAME)
public class StrawberryPersonTopology implements AbstractTopology<StrawberryPersonId, StrawberryPersonProjectionEvent> {

  PrismaClient prismaClient;
  PrismaMutationResolver mutationResolver;

  @EventHandler
  public void process(StrawberryPersonProjectionEvent projectionEvent) {
    log.info("Projecting {}(identity={}), value: {}", projectionEvent.getClass().getSimpleName(), projectionEvent.identity(), projectionEvent);

    CreateSPersonMutation createSPersonMutation = CreateSPersonMutation.builder()
        .data(SPersonCreateInput.builder()
            .id(Cuid.createCuid())
            .coreID(projectionEvent.identity().value())
            .firstName(projectionEvent.firstName())
            .lastName(projectionEvent.lastName())
            .username(projectionEvent.userName())
            .isAdmin(projectionEvent.isAdmin())
            .removed(projectionEvent.removed())
            ._createdAt(projectionEvent.createdAt())
            .modifiedAt(projectionEvent.modifiedAt())
            .build())
        .build();

    Mutation mutation = mutationResolver.resolveMutation(projectionEvent, createSPersonMutation, null);
    prismaClient.mutate(mutation);
  }

  @Override
  public String topologyName() {
    return PERSON_TOPOLOGY_NAME;
  }

  @Override
  public ImmutableSet<ProjectionIndex<StrawberryPersonProjectionEvent>> indices() {
    return IStrawberryPersonProjectionEvent.INDICES;
  }

  @Override
  public ProjectionEventStream<StrawberryPersonId, StrawberryPersonProjectionEvent> eventStream() {
    return IStrawberryPersonProjectionEvent.eventStream();
  }
}
