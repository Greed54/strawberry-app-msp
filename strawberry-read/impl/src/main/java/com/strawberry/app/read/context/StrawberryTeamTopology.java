package com.strawberry.app.read.context;

import static com.strawberry.app.read.context.utils.TopologyNames.TEAM_TOPOLOGY_NAME;

import com.apollographql.apollo.api.Mutation;
import com.google.common.collect.ImmutableSet;
import com.stawberry.app.read.prisma.graphql.CreateSTeamMutation;
import com.stawberry.app.read.prisma.graphql.UpdateSTeamMutation;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeCreateInput;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeCreateOneInput;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeUpdateOneInput;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeWhereUniqueInput;
import com.stawberry.app.read.prisma.graphql.type.SPersonCreateOneInput;
import com.stawberry.app.read.prisma.graphql.type.SPersonUpdateOneInput;
import com.stawberry.app.read.prisma.graphql.type.SPersonWhereUniqueInput;
import com.stawberry.app.read.prisma.graphql.type.STeamCreateInput;
import com.stawberry.app.read.prisma.graphql.type.STeamUpdateInput;
import com.stawberry.app.read.prisma.graphql.type.STeamWhereUniqueInput;
import com.strawberry.app.common.cqengine.ProjectionIndex;
import com.strawberry.app.common.projection.ProjectionEventStream;
import com.strawberry.app.common.topology.AbstractTopology;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.team.projection.IStrawberryTeamProjectionEvent;
import com.strawberry.app.core.context.team.projection.StrawberryTeamProjectionEvent;
import com.strawberry.app.read.apollo.PrismaClient;
import com.strawberry.app.read.context.utils.PrismaMutationResolver;
import cool.graph.cuid.Cuid;
import java.util.Optional;
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
@ProcessingGroup(TEAM_TOPOLOGY_NAME)
public class StrawberryTeamTopology implements AbstractTopology<StrawberryTeamId, StrawberryTeamProjectionEvent> {

  PrismaClient prismaClient;
  PrismaMutationResolver mutationResolver;

  @EventHandler
  public void process(StrawberryTeamProjectionEvent projectionEvent) {
    log.info("Projecting {}(identity={}), value: {}", projectionEvent.getClass().getSimpleName(), projectionEvent.identity(), projectionEvent);

    CreateSTeamMutation createSTeamMutation = CreateSTeamMutation.builder()
        .data(STeamCreateInput.builder()
            .id(Cuid.createCuid())
            .coreID(projectionEvent.identity().value())
            .teamName(projectionEvent.teamName())
            .teamLead(Optional.ofNullable(projectionEvent.teamLeadId()).map(teamLeadId -> SEmployeeCreateOneInput.builder()
                .connect(SEmployeeWhereUniqueInput.builder()
                    .coreID(teamLeadId.value())
                    .build())
                .create(SEmployeeCreateInput.builder()
                    .coreID(teamLeadId.value())
                    .build())
                .build()).orElse(null))
            .removed(projectionEvent.removed())
            ._createdAt(projectionEvent.createdAt())
            .createdBy(Optional.ofNullable(projectionEvent.createdBy())
                .map(personId -> SPersonCreateOneInput.builder()
                    .connect(SPersonWhereUniqueInput.builder()
                        .coreID(personId.value())
                        .build())
                    .build())
                .orElse(null))
            .build())
        .build();

    UpdateSTeamMutation updateSTeamMutation = UpdateSTeamMutation.builder()
        .data(STeamUpdateInput.builder()
            .teamName(projectionEvent.teamName())
            .teamLead(Optional.ofNullable(projectionEvent.teamLeadId()).map(teamLeadId -> SEmployeeUpdateOneInput.builder()
                .connect(SEmployeeWhereUniqueInput.builder()
                    .coreID(teamLeadId.value())
                    .build())
                .create(SEmployeeCreateInput.builder()
                    .coreID(teamLeadId.value())
                    .build())
                .build()).orElse(null))
            .removed(projectionEvent.removed())
            .modifiedAt(projectionEvent.modifiedAt())
            .modifiedBy(Optional.ofNullable(projectionEvent.modifiedBy())
                .map(personId -> SPersonUpdateOneInput.builder()
                    .connect(SPersonWhereUniqueInput.builder()
                        .coreID(personId.value())
                        .build())
                    .build())
                .orElse(null))
            .build())
        .where(STeamWhereUniqueInput.builder()
            .coreID(projectionEvent.identity().value())
            .build())
        .build();

    Mutation mutation = mutationResolver.resolveMutation(projectionEvent, createSTeamMutation, updateSTeamMutation);
    prismaClient.mutate(mutation);
  }

  @Override
  public String topologyName() {
    return TEAM_TOPOLOGY_NAME;
  }

  @Override
  public ImmutableSet<ProjectionIndex<StrawberryTeamProjectionEvent>> indices() {
    return IStrawberryTeamProjectionEvent.INDICES;
  }

  @Override
  public ProjectionEventStream<StrawberryTeamId, StrawberryTeamProjectionEvent> eventStream() {
    return IStrawberryTeamProjectionEvent.eventStream();
  }
}
