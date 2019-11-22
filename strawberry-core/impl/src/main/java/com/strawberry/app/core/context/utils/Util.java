package com.strawberry.app.core.context.utils;

import com.strawberry.app.core.context.common.property.context.identity.BaseStringId;
import com.strawberry.app.core.context.cqrscommon.Identity;
import com.strawberry.app.core.context.employee.projection.StrawberryEmployeeProjectionEntity;
import com.strawberry.app.core.context.employee.projection.StrawberryEmployeeProjectionEvent;
import com.strawberry.app.core.context.team.projection.StrawberryTeamProjectionEntity;
import com.strawberry.app.core.context.team.projection.StrawberryTeamProjectionEvent;
import java.util.Optional;

public final class Util {

  private Util() {
  }

  public static <V extends BaseStringId> String mapNullableIdToString(V id) {
    if (Optional.ofNullable(id).isPresent()) {
      return id.value();
    }
    return null;
  }

  public static <V extends Identity<?>> String mapNullableIdToString(V id) {
    if (Optional.ofNullable(id).isPresent()) {
      return (String) id.value();
    }
    return null;
  }

  public static StrawberryEmployeeProjectionEntity mapEmployeeProjectionEvent(StrawberryEmployeeProjectionEvent event) {
    return StrawberryEmployeeProjectionEntity.builder()
        .identity(event.identity().value())
        .cardId(event.cardId().value())
        .firstName(event.firstName())
        .lastName(event.lastName())
        .employeeRole(Optional.ofNullable(event.employeeRole()).toString())
        .teamId(event.teamId().value())
        .note(event.note())
        .removed(event.removed())
        .createdAt(event.createdAt())
        .createdBy(mapNullableIdToString(event.createdBy()))
        .modifiedAt(event.modifiedAt())
        .modifiedBy(mapNullableIdToString(event.modifiedBy()))
        .build();
  }

  public static StrawberryTeamProjectionEntity mapTeamProjectionEvent(StrawberryTeamProjectionEvent event) {
    return StrawberryTeamProjectionEntity.builder()
        .identity(event.identity().value())
        .teamName(event.teamName())
        .teamLeadId(mapNullableIdToString(event.teamLeadId()))
        .removed(event.removed())
        .createdAt(event.createdAt())
        .createdBy(mapNullableIdToString(event.createdBy()))
        .modifiedAt(event.modifiedAt())
        .modifiedBy(mapNullableIdToString(event.modifiedBy()))
        .build();
  }
}
