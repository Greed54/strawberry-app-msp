package com.strawberry.app.read.context;

import com.apollographql.apollo.api.Mutation;
import com.stawberry.app.read.prisma.graphql.CreateSWorkDayMutation;
import com.stawberry.app.read.prisma.graphql.UpdateSWorkDayMutation;
import com.stawberry.app.read.prisma.graphql.type.STeamCreateInput;
import com.stawberry.app.read.prisma.graphql.type.STeamCreateManyInput;
import com.stawberry.app.read.prisma.graphql.type.STeamUpdateManyInput;
import com.stawberry.app.read.prisma.graphql.type.STeamWhereUniqueInput;
import com.stawberry.app.read.prisma.graphql.type.SWorkDayCreateInput;
import com.stawberry.app.read.prisma.graphql.type.SWorkDayUpdateInput;
import com.stawberry.app.read.prisma.graphql.type.SWorkDayWhereUniqueInput;
import com.strawberry.app.common.property.context.identity.BaseStringId;
import com.strawberry.app.core.context.workday.projection.StrawberryWorkDayProjectionEvent;
import com.strawberry.app.read.apollo.PrismaClient;
import com.strawberry.app.read.context.utils.PrismaMutationResolver;
import cool.graph.cuid.Cuid;
import java.util.Optional;
import java.util.stream.Collectors;
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
public class StrawberryWorkDayTopology {

  Logger LOGGER = LoggerFactory.getLogger(StrawberryWorkDayTopology.class);

  PrismaClient prismaClient;
  PrismaMutationResolver mutationResolver;

  @EventHandler
  public void on(StrawberryWorkDayProjectionEvent projectionEvent) {
    LOGGER.info("Projecting {}(identity={}), value: {}", projectionEvent.getClass().getSimpleName(), projectionEvent.identity(), projectionEvent);

    CreateSWorkDayMutation createSWorkDayMutation = CreateSWorkDayMutation.builder()
        .data(SWorkDayCreateInput.builder()
            .id(Cuid.createCuid())
            .coreID(projectionEvent.identity().value())
            .date(projectionEvent.date())
            .teams(STeamCreateManyInput.builder()
                .connect(projectionEvent.teamIds().stream()
                    .map(teamId -> STeamWhereUniqueInput.builder().coreID(teamId.value()).build())
                    .collect(Collectors.toList()))
                .create(projectionEvent.teamIds().stream()
                    .map(teamId -> STeamCreateInput.builder().coreID(teamId.value()).build())
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
                .create(projectionEvent.teamIds().stream()
                    .map(teamId -> STeamCreateInput.builder().coreID(teamId.value()).build())
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
}
