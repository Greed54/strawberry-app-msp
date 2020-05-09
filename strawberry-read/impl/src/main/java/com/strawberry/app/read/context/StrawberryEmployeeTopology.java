package com.strawberry.app.read.context;

import static com.strawberry.app.read.context.utils.TopologyNames.EMPLOYEE_TOPOLOGY_NAME;

import com.apollographql.apollo.api.Mutation;
import com.google.common.collect.ImmutableSet;
import com.stawberry.app.read.prisma.graphql.CreateSEmployeeMutation;
import com.stawberry.app.read.prisma.graphql.UpdateSEmployeeMutation;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeCreateInput;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeUpdateInput;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeWhereUniqueInput;
import com.stawberry.app.read.prisma.graphql.type.SPersonCreateOneInput;
import com.stawberry.app.read.prisma.graphql.type.SPersonUpdateOneInput;
import com.stawberry.app.read.prisma.graphql.type.SPersonWhereUniqueInput;
import com.stawberry.app.read.prisma.graphql.type.STeamCreateOneWithoutEmployeesInput;
import com.stawberry.app.read.prisma.graphql.type.STeamUpdateOneWithoutEmployeesInput;
import com.stawberry.app.read.prisma.graphql.type.STeamWhereUniqueInput;
import com.strawberry.app.common.cqengine.ProjectionIndex;
import com.strawberry.app.common.projection.ProjectionEventStream;
import com.strawberry.app.common.topology.AbstractTopology;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.projection.IStrawberryEmployeeProjectionEvent;
import com.strawberry.app.core.context.employee.projection.StrawberryEmployeeProjectionEvent;
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
@ProcessingGroup(EMPLOYEE_TOPOLOGY_NAME)
public class StrawberryEmployeeTopology implements AbstractTopology<StrawberryEmployeeId, StrawberryEmployeeProjectionEvent> {

  PrismaClient prismaClient;
  PrismaMutationResolver mutationResolver;

  @EventHandler
  public void process(StrawberryEmployeeProjectionEvent projectionEvent) {
    log.info("Projecting {}(identity={}), value: {}", projectionEvent.getClass().getSimpleName(), projectionEvent.identity(), projectionEvent);

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
            .person(SPersonCreateOneInput.builder()
                .connect(SPersonWhereUniqueInput.builder()
                    .coreID(projectionEvent.personId().value())
                    .build())
                .build())
            .createdBy(Optional.ofNullable(projectionEvent.createdBy())
                .map(personId -> SPersonCreateOneInput.builder()
                    .connect(SPersonWhereUniqueInput.builder()
                        .coreID(personId.value())
                        .build())
                    .build())
                .orElse(null))
            ._createdAt(projectionEvent.createdAt())
            .removed(projectionEvent.removed())
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
            .person(SPersonUpdateOneInput.builder()
                .connect(SPersonWhereUniqueInput.builder()
                    .coreID(projectionEvent.personId().value())
                    .build())
                .build())
            .modifiedBy(Optional.ofNullable(projectionEvent.modifiedBy())
                .map(personId -> SPersonUpdateOneInput.builder()
                    .connect(SPersonWhereUniqueInput.builder()
                        .coreID(personId.value())
                        .build())
                    .build())
                .orElse(null))
            .modifiedAt(projectionEvent.modifiedAt())
            .removed(projectionEvent.removed())
            .build())
        .where(SEmployeeWhereUniqueInput.builder()
            .coreID(projectionEvent.identity().value())
            .build())
        .build();

    Mutation mutation = mutationResolver.resolveMutation(projectionEvent, createEmployeeMutation, updateEmployeeMutation);
    prismaClient.mutate(mutation);
  }

  @Override
  public String topologyName() {
    return EMPLOYEE_TOPOLOGY_NAME;
  }

  @Override
  public ImmutableSet<ProjectionIndex<StrawberryEmployeeProjectionEvent>> indices() {
    return IStrawberryEmployeeProjectionEvent.INDICES;
  }

  @Override
  public ProjectionEventStream<StrawberryEmployeeId, StrawberryEmployeeProjectionEvent> eventStream() {
    return IStrawberryEmployeeProjectionEvent.eventStream();
  }
}
