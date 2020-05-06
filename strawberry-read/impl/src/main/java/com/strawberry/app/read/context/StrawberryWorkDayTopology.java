package com.strawberry.app.read.context;

import static com.strawberry.app.read.context.utils.TopologyNames.WORK_DAY_TOPOLOGY_NAME;

import com.apollographql.apollo.api.Mutation;
import com.google.common.collect.ImmutableSet;
import com.stawberry.app.read.prisma.graphql.CreateSWorkDayMutation;
import com.stawberry.app.read.prisma.graphql.UpdateSWorkDayMutation;
import com.stawberry.app.read.prisma.graphql.type.STeamCreateManyInput;
import com.stawberry.app.read.prisma.graphql.type.STeamUpdateManyInput;
import com.stawberry.app.read.prisma.graphql.type.STeamWhereUniqueInput;
import com.stawberry.app.read.prisma.graphql.type.SWorkDayCreateInput;
import com.stawberry.app.read.prisma.graphql.type.SWorkDayUpdateInput;
import com.stawberry.app.read.prisma.graphql.type.SWorkDayWhereUniqueInput;
import com.strawberry.app.common.cqengine.ProjectionIndex;
import com.strawberry.app.common.projection.ProjectionEventStream;
import com.strawberry.app.common.property.context.identity.BaseStringId;
import com.strawberry.app.common.topology.AbstractTopology;
import com.strawberry.app.core.context.workday.identities.StrawberryWorkDayId;
import com.strawberry.app.core.context.workday.projection.IStrawberryWorkDayProjectionEvent;
import com.strawberry.app.core.context.workday.projection.StrawberryWorkDayProjectionEvent;
import com.strawberry.app.read.apollo.PrismaClient;
import com.strawberry.app.read.context.utils.PrismaMutationResolver;
import cool.graph.cuid.Cuid;
import java.util.Optional;
import java.util.stream.Collectors;
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
@ProcessingGroup(WORK_DAY_TOPOLOGY_NAME)
public class StrawberryWorkDayTopology implements AbstractTopology<StrawberryWorkDayId, StrawberryWorkDayProjectionEvent> {

  PrismaClient prismaClient;
  PrismaMutationResolver mutationResolver;

  @EventHandler
  public void process(StrawberryWorkDayProjectionEvent projectionEvent) {
    log.info("Projecting {}(identity={}), value: {}", projectionEvent.getClass().getSimpleName(), projectionEvent.identity(), projectionEvent);

    CreateSWorkDayMutation createSWorkDayMutation = CreateSWorkDayMutation.builder()
        .data(SWorkDayCreateInput.builder()
            .id(Cuid.createCuid())
            .coreID(projectionEvent.identity().value())
            .date(projectionEvent.date())
            .teams(STeamCreateManyInput.builder()
                .connect(projectionEvent.teamIds().stream()
                    .map(teamId -> STeamWhereUniqueInput.builder().coreID(teamId.value()).build())
                    .collect(Collectors.toList()))
                .build())
            .pricePerKilo(projectionEvent.pricePerKilogram())
            .tareWeight(projectionEvent.tareWeight())
            ._createdAt(projectionEvent.createdAt())
            .modifiedAt(projectionEvent.modifiedAt())
            .modifiedBy(Optional.ofNullable(projectionEvent.modifiedBy()).map(BaseStringId::value).orElse(null))
            .removed(projectionEvent.removed())
            .build())
        .build();

    UpdateSWorkDayMutation updateSWorkDayMutation = UpdateSWorkDayMutation.builder()
        .data(SWorkDayUpdateInput.builder()
            .teams(STeamUpdateManyInput.builder()
                .connect(projectionEvent.teamIds().stream()
                    .map(teamId -> STeamWhereUniqueInput.builder().coreID(teamId.value()).build())
                    .collect(Collectors.toList()))
                .build())
            .pricePerKilo(projectionEvent.pricePerKilogram())
            .tareWeight(projectionEvent.tareWeight())
            ._createdAt(projectionEvent.createdAt())
            .modifiedAt(projectionEvent.modifiedAt())
            .modifiedBy(Optional.ofNullable(projectionEvent.modifiedBy()).map(BaseStringId::value).orElse(null))
            .removed(projectionEvent.removed())
            .build())
        .where(SWorkDayWhereUniqueInput.builder()
            .coreID(projectionEvent.identity().value())
            .build())
        .build();

    Mutation mutation = mutationResolver.resolveMutation(projectionEvent, createSWorkDayMutation, updateSWorkDayMutation);
    prismaClient.mutate(mutation);
  }

  @Override
  public String topologyName() {
    return WORK_DAY_TOPOLOGY_NAME;
  }

  @Override
  public ImmutableSet<ProjectionIndex<StrawberryWorkDayProjectionEvent>> indices() {
    return IStrawberryWorkDayProjectionEvent.INDICES;
  }

  @Override
  public ProjectionEventStream<StrawberryWorkDayId, StrawberryWorkDayProjectionEvent> eventStream() {
    return IStrawberryWorkDayProjectionEvent.eventStream();
  }
}
