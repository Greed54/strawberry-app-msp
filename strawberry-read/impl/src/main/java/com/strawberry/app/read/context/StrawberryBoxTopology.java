package com.strawberry.app.read.context;

import static com.strawberry.app.read.context.utils.TopologyNames.BOX_TOPOLOGY_NAME;

import com.apollographql.apollo.api.Mutation;
import com.google.common.collect.ImmutableSet;
import com.stawberry.app.read.prisma.graphql.CreateSBoxMutation;
import com.stawberry.app.read.prisma.graphql.UpdateSBoxMutation;
import com.stawberry.app.read.prisma.graphql.type.SBoxCreateInput;
import com.stawberry.app.read.prisma.graphql.type.SBoxUpdateInput;
import com.stawberry.app.read.prisma.graphql.type.SBoxWhereUniqueInput;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeCreateOneWithoutBoxesInput;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeUpdateOneWithoutBoxesInput;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeWhereUniqueInput;
import com.stawberry.app.read.prisma.graphql.type.SPersonCreateOneInput;
import com.stawberry.app.read.prisma.graphql.type.SPersonUpdateOneInput;
import com.stawberry.app.read.prisma.graphql.type.SPersonWhereUniqueInput;
import com.stawberry.app.read.prisma.graphql.type.SWorkDayUpdateOneInput;
import com.stawberry.app.read.prisma.graphql.type.SWorkDayWhereUniqueInput;
import com.strawberry.app.common.cqengine.ProjectionIndex;
import com.strawberry.app.common.projection.ProjectionEventStream;
import com.strawberry.app.common.topology.AbstractTopology;
import com.strawberry.app.core.context.box.identities.StrawberryBoxId;
import com.strawberry.app.core.context.box.projecton.IStrawberryBoxProjectionEvent;
import com.strawberry.app.core.context.box.projecton.StrawberryBoxProjectionEvent;
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
@ProcessingGroup(BOX_TOPOLOGY_NAME)
public class StrawberryBoxTopology implements AbstractTopology<StrawberryBoxId, StrawberryBoxProjectionEvent> {

  PrismaClient prismaClient;
  PrismaMutationResolver mutationResolver;

  @EventHandler
  public void process(StrawberryBoxProjectionEvent projectionEvent) {
    log.info("Projecting {}(identity={}), value: {}", projectionEvent.getClass().getSimpleName(), projectionEvent.identity(), projectionEvent);

    CreateSBoxMutation createSBoxMutation = CreateSBoxMutation.builder()
        .data(SBoxCreateInput.builder()
            .id(Cuid.createCuid())
            .coreID(projectionEvent.identity().value())
            .employee(SEmployeeCreateOneWithoutBoxesInput.builder()
                .connect(SEmployeeWhereUniqueInput.builder()
                    .coreID(projectionEvent.employeeId().value())
                    .build())
                .build())
            .kilograms(projectionEvent.kilograms())
            .boxAmount(projectionEvent.boxAmount())
            .weightId(projectionEvent.weightId())
            ._createdAt(projectionEvent.createdAt())
            .modifiedAt(projectionEvent.modifiedAt())
            .modifiedBy(Optional.ofNullable(projectionEvent.modifiedBy())
                .map(personId -> SPersonCreateOneInput.builder()
                    .connect(SPersonWhereUniqueInput.builder()
                        .coreID(personId.value())
                        .build())
                    .build())
                .orElse(null))
            .removed(projectionEvent.removed())
            .build())
        .build();

    UpdateSBoxMutation updateSBoxMutation = UpdateSBoxMutation.builder()
        .data(SBoxUpdateInput.builder()
            .employee(SEmployeeUpdateOneWithoutBoxesInput.builder()
                .connect(SEmployeeWhereUniqueInput.builder()
                    .coreID(projectionEvent.employeeId().value())
                    .build())
                .build())
            .workDay(Optional.ofNullable(projectionEvent.workDayId()).map(workDayId -> SWorkDayUpdateOneInput.builder()
                .connect(SWorkDayWhereUniqueInput.builder()
                    .coreID(workDayId.value())
                    .build())
                .build()).orElse(null))
            .kilograms(projectionEvent.kilograms())
            .boxAmount(projectionEvent.boxAmount())
            .weightId(projectionEvent.weightId())
            ._createdAt(projectionEvent.createdAt())
            .modifiedAt(projectionEvent.modifiedAt())
            .modifiedBy(Optional.ofNullable(projectionEvent.modifiedBy())
                .map(personId -> SPersonUpdateOneInput.builder()
                    .connect(SPersonWhereUniqueInput.builder()
                        .coreID(personId.value())
                        .build())
                    .build())
                .orElse(null))
            .removed(projectionEvent.removed())
            .build())
        .where(SBoxWhereUniqueInput.builder()
            .coreID(projectionEvent.identity().value())
            .build())
        .build();

    Mutation mutation = mutationResolver.resolveMutation(projectionEvent, createSBoxMutation, updateSBoxMutation);
    prismaClient.mutate(mutation);
  }

  @Override
  public String topologyName() {
    return BOX_TOPOLOGY_NAME;
  }

  @Override
  public ImmutableSet<ProjectionIndex<StrawberryBoxProjectionEvent>> indices() {
    return IStrawberryBoxProjectionEvent.INDICES;
  }

  @Override
  public ProjectionEventStream<StrawberryBoxId, StrawberryBoxProjectionEvent> eventStream() {
    return IStrawberryBoxProjectionEvent.eventStream();
  }
}
