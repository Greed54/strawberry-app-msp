package com.strawberry.app.read.context;

import com.apollographql.apollo.api.Mutation;
import com.stawberry.app.read.prisma.graphql.CreateSEmployeeMutation;
import com.stawberry.app.read.prisma.graphql.UpdateSEmployeeMutation;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeCreateInput;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeUpdateInput;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeWhereUniqueInput;
import com.stawberry.app.read.prisma.graphql.type.STeamCreateOneWithoutEmployeesInput;
import com.stawberry.app.read.prisma.graphql.type.STeamUpdateOneWithoutEmployeesInput;
import com.stawberry.app.read.prisma.graphql.type.STeamWhereUniqueInput;
import com.strawberry.app.common.property.context.identity.BaseStringId;
import com.strawberry.app.core.context.employee.projection.StrawberryEmployeeProjectionEvent;
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
public class StrawberryEmployeeTopology {

  Logger LOGGER = LoggerFactory.getLogger(StrawberryEmployeeTopology.class);

  PrismaClient prismaClient;
  PrismaMutationResolver mutationResolver;

  @EventHandler
  public void on(StrawberryEmployeeProjectionEvent projectionEvent) {
    LOGGER.info("Projecting {}(identity={}), value: {}", projectionEvent.getClass().getSimpleName(), projectionEvent.identity(), projectionEvent);

    CreateSEmployeeMutation createEmployeeMutation = CreateSEmployeeMutation.builder()
        .data(SEmployeeCreateInput.builder()
            .id(Cuid.createCuid())
            .coreID(projectionEvent.identity().value())
            .cardId(projectionEvent.cardId().value())
            .firstName(projectionEvent.firstName())
            .lastName(projectionEvent.lastName())
            .employeeRole(Optional.ofNullable(projectionEvent.employeeRole()).map(Enum::name).orElse(null))
            .note(projectionEvent.note())
            .team(STeamCreateOneWithoutEmployeesInput.builder()
                .connect(STeamWhereUniqueInput.builder()
                    .coreID(projectionEvent.teamId().value())
                    .build())
                .build())
            .createdBy(Optional.ofNullable(projectionEvent.createdBy()).map(BaseStringId::value).orElse(null))
            ._createdAt(projectionEvent.createdAt())
            .build())
        .build();

    UpdateSEmployeeMutation updateEmployeeMutation = UpdateSEmployeeMutation.builder()
        .data(SEmployeeUpdateInput.builder()
            .cardId(projectionEvent.cardId().value())
            .firstName(projectionEvent.firstName())
            .lastName(projectionEvent.lastName())
            .note(projectionEvent.note())
            .employeeRole(Optional.ofNullable(projectionEvent.employeeRole()).map(Enum::name).orElse(null))
            .team(STeamUpdateOneWithoutEmployeesInput.builder()
                .connect(STeamWhereUniqueInput.builder()
                    .coreID(projectionEvent.teamId().value())
                    .build())
                .build())
            .modifiedBy(Optional.ofNullable(projectionEvent.modifiedBy()).map(BaseStringId::value).orElse(null))
            .modifiedAt(projectionEvent.modifiedAt())
            .build())
        .where(SEmployeeWhereUniqueInput.builder()
            .coreID(projectionEvent.identity().value())
            .build())
        .build();

    Mutation mutation = mutationResolver.resolveMutation(projectionEvent, createEmployeeMutation, updateEmployeeMutation);
    prismaClient.mutate(mutation);
  }
}
