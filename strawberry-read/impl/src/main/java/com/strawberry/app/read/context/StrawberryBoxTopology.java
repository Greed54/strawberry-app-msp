package com.strawberry.app.read.context;

import com.apollographql.apollo.api.Mutation;
import com.stawberry.app.read.prisma.graphql.CreateSBoxMutation;
import com.stawberry.app.read.prisma.graphql.UpdateSBoxMutation;
import com.stawberry.app.read.prisma.graphql.type.SBoxCreateInput;
import com.stawberry.app.read.prisma.graphql.type.SBoxUpdateInput;
import com.stawberry.app.read.prisma.graphql.type.SBoxWhereUniqueInput;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeCreateInput;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeCreateOneInput;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeUpdateOneInput;
import com.stawberry.app.read.prisma.graphql.type.SEmployeeWhereUniqueInput;
import com.stawberry.app.read.prisma.graphql.type.SWorkDayCreateInput;
import com.stawberry.app.read.prisma.graphql.type.SWorkDayUpdateOneInput;
import com.stawberry.app.read.prisma.graphql.type.SWorkDayWhereUniqueInput;
import com.strawberry.app.common.property.context.identity.BaseStringId;
import com.strawberry.app.core.context.box.projecton.StrawberryBoxProjectionEvent;
import com.strawberry.app.read.apollo.PrismaClient;
import com.strawberry.app.read.context.utils.PrismaMutationResolver;
import cool.graph.cuid.Cuid;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ProcessingGroup("StrawberryBoxProjectionEvent")
public class StrawberryBoxTopology {

  Logger LOGGER = LoggerFactory.getLogger(StrawberryBoxTopology.class);

  PrismaClient prismaClient;
  PrismaMutationResolver mutationResolver;

  @EventHandler
  public void on(StrawberryBoxProjectionEvent projectionEvent) {
    LOGGER.info("Projecting {}(identity={}), value: {}", projectionEvent.getClass().getSimpleName(), projectionEvent.identity(), projectionEvent);

    CreateSBoxMutation createSBoxMutation = CreateSBoxMutation.builder()
        .data(SBoxCreateInput.builder()
            .id(Cuid.createCuid())
            .coreID(projectionEvent.identity().value())
            .employee(SEmployeeCreateOneInput.builder()
                .connect(SEmployeeWhereUniqueInput.builder()
                    .coreID(projectionEvent.employeeId().value())
                    .build())
                .build())
            .kilograms(projectionEvent.kilograms())
            .boxAmount(projectionEvent.boxAmount())
            .weightId(projectionEvent.weightId())
            ._createdAt(projectionEvent.createdAt())
            .modifiedAt(projectionEvent.modifiedAt())
            .modifiedBy(Optional.ofNullable(projectionEvent.modifiedBy()).map(BaseStringId::value).orElse(null))
            .removed(projectionEvent.removed())
            .build())
        .build();

    UpdateSBoxMutation updateSBoxMutation = UpdateSBoxMutation.builder()
        .data(SBoxUpdateInput.builder()
            .employee(SEmployeeUpdateOneInput.builder()
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
            .modifiedBy(Optional.ofNullable(projectionEvent.modifiedBy()).map(BaseStringId::value).orElse(null))
            .removed(projectionEvent.removed())
            .build())
        .where(SBoxWhereUniqueInput.builder()
            .coreID(projectionEvent.identity().value())
            .build())
        .build();

    Mutation mutation = mutationResolver.resolveMutation(projectionEvent, createSBoxMutation, updateSBoxMutation);
    prismaClient.mutate(mutation);
  }
}
