package com.strawberry.app.read.context;

import com.apollographql.apollo.ApolloCall.Callback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.stawberry.app.read.prisma.graphql.CreateEmployeeMutation;
import com.stawberry.app.read.prisma.graphql.CreateEmployeeMutation.Data;
import com.stawberry.app.read.prisma.graphql.UpdateEmployeeMutation;
import com.stawberry.app.read.prisma.graphql.type.EmployeeCreateInput;
import com.stawberry.app.read.prisma.graphql.type.EmployeeUpdateInput;
import com.stawberry.app.read.prisma.graphql.type.EmployeeWhereUniqueInput;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAddedEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAmendedEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAmendedNoteEvent;
import com.strawberry.app.core.context.employee.event.StrawberryEmployeeAmendedRoleEvent;
import com.strawberry.app.read.apollo.PrismaClient;
import cool.graph.cuid.Cuid;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.axonframework.eventhandling.EventHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StrawberryEmployeeProjector {

  Logger LOGGER = LoggerFactory.getLogger(StrawberryEmployeeProjector.class);

  PrismaClient prismaClient;

  @EventHandler
  public void on(StrawberryEmployeeAddedEvent event) {
    LOGGER.info("Projecting {}(identity={}), value: {}", event.getClass().getSimpleName(), event.identity(), event);

    CreateEmployeeMutation createEmployeeMutation = CreateEmployeeMutation.builder()
        .data(EmployeeCreateInput.builder()
            .id(Cuid.createCuid())
            .coreID(event.identity().value())
            .cardId(event.cardId().value())
            .firstName(event.firstName())
            .lastName(event.lastName())
            .employeeRole(event.employeeRole().toString())
            .note(event.note())
            .createdBy(event.createdBy().value())
            ._createdAt(event.createdAt())
            .build())
        .build();

    prismaClient.getApolloClient()
        .mutate(createEmployeeMutation)
        .enqueue(new Callback<>() {
          @Override
          public void onResponse(@NotNull Response<Optional<Data>> response) {
            if (Objects.requireNonNull(response.data()).isPresent()) {
              response.data().ifPresent(data -> LOGGER.info(data.createEmployee().toString()));
            }
            if (!response.errors().isEmpty()) {
              LOGGER.warn(response.errors().toString());
            }
          }

          @Override
          public void onFailure(@NotNull ApolloException e) {
            LOGGER.error(e.getMessage());
          }
        });
  }

  @EventHandler
  public void on(StrawberryEmployeeAmendedEvent event) {
    LOGGER.info("Projecting {}(identity={}), value: {}", event.getClass().getSimpleName(), event.identity(), event);

    UpdateEmployeeMutation updateEmployeeMutation = UpdateEmployeeMutation.builder()
        .data(EmployeeUpdateInput.builder()
            .cardId(event.cardId().value())
            .firstName(event.firstName())
            .lastName(event.lastName())
            .note(event.note())
            .modifiedBy(event.modifiedBy().value())
            .modifiedAt(event.modifiedAt())
            .build())
        .where(EmployeeWhereUniqueInput.builder()
            .coreID(event.identity().value())
            .build())
        .build();

    sendUpdate(updateEmployeeMutation);
  }

  @EventHandler
  public void on(StrawberryEmployeeAmendedRoleEvent event) {
    LOGGER.info("Projecting {}(identity={}), value: {}", event.getClass().getSimpleName(), event.identity(), event);

    UpdateEmployeeMutation amendRoleMutation = UpdateEmployeeMutation.builder()
        .data(EmployeeUpdateInput.builder()
            .employeeRole(event.employeeRole().toString())
            .modifiedBy(event.modifiedBy().value())
            .modifiedAt(event.modifiedAt())
            .build())
        .where(EmployeeWhereUniqueInput.builder()
            .coreID(event.identity().value())
            .build())
        .build();

    sendUpdate(amendRoleMutation);
  }

  @EventHandler
  public void on(StrawberryEmployeeAmendedNoteEvent event) {
    LOGGER.info("Projecting {}(identity={}), value: {}", event.getClass().getSimpleName(), event.identity(), event);

    UpdateEmployeeMutation amendNoteMutation = UpdateEmployeeMutation.builder()
        .data(EmployeeUpdateInput.builder()
            .note(event.note())
            .modifiedBy(event.modifiedBy().value())
            .modifiedAt(event.modifiedAt())
            .build())
        .where(EmployeeWhereUniqueInput.builder()
            .coreID(event.identity().value())
            .build())
        .build();

    sendUpdate(amendNoteMutation);
  }

  private void sendUpdate(UpdateEmployeeMutation updateEmployeeMutation) {
    prismaClient.getApolloClient()
        .mutate(updateEmployeeMutation)
        .enqueue(new Callback<>() {
          @Override
          public void onResponse(@NotNull Response<Optional<UpdateEmployeeMutation.Data>> response) {
            if (Objects.requireNonNull(response.data()).isPresent()) {
              response.data().ifPresent(data -> LOGGER.info(data.updateEmployee().toString()));
            }
            if (!response.errors().isEmpty()) {
              LOGGER.warn(response.errors().toString());
            }
          }

          @Override
          public void onFailure(@NotNull ApolloException e) {
            LOGGER.error(e.getMessage());
          }
        });
  }
}
