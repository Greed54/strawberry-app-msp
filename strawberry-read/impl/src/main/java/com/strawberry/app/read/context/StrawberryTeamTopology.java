package com.strawberry.app.read.context;

import com.apollographql.apollo.api.Mutation;
import com.stawberry.app.read.prisma.graphql.CreateSTeamMutation;
import com.stawberry.app.read.prisma.graphql.UpdateSTeamMutation;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeCreateInput;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeCreateOneInput;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeUpdateOneInput;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeWhereUniqueInput;
import com.stawberry.app.read.prisma.graphql.type.STeamCreateInput;
import com.stawberry.app.read.prisma.graphql.type.STeamUpdateInput;
import com.stawberry.app.read.prisma.graphql.type.STeamWhereUniqueInput;
import com.strawberry.app.common.property.context.identity.BaseStringId;
import com.strawberry.app.core.context.team.projection.StrawberryTeamProjectionEvent;
import com.strawberry.app.read.apollo.PrismaClient;
import com.strawberry.app.read.context.utils.PrismaMutationResolver;
import cool.graph.cuid.Cuid;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrawberryTeamTopology {

  Logger LOGGER = LoggerFactory.getLogger(StrawberryTeamTopology.class);

  PrismaClient prismaClient;
  PrismaMutationResolver mutationResolver;

  @EventHandler
  public void on(StrawberryTeamProjectionEvent projectionEvent) {
    LOGGER.info("Projecting {}(identity={}), value: {}", projectionEvent.getClass().getSimpleName(), projectionEvent.identity(), projectionEvent);

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
            .createdBy(Optional.ofNullable(projectionEvent.createdBy()).map(BaseStringId::value).orElse(null))
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
            .modifiedBy(Optional.ofNullable(projectionEvent.modifiedBy()).map(BaseStringId::value).orElse(null))
            .build())
        .where(STeamWhereUniqueInput.builder()
            .coreID(projectionEvent.identity().value())
            .build())
        .build();

    Mutation mutation = mutationResolver.resolveMutation(projectionEvent, createSTeamMutation, updateSTeamMutation);
    prismaClient.mutate(mutation);
  }
}
