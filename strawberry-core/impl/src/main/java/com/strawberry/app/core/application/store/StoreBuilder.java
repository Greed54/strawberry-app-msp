package com.strawberry.app.core.application.store;

import com.strawberry.app.core.context.common.cqengine.indexedstore.IndexedStoreBuilder;
import com.strawberry.app.core.context.common.cqengine.indexedstore.IndexedStoreImpl;
import com.strawberry.app.core.context.employee.StrawberryEmployee;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.team.StrawberryTeam;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StoreBuilder {

  public IndexedStoreImpl<StrawberryEmployeeId, StrawberryEmployee> buildStrawberryEmployeeStore() {
    IndexedStoreBuilder<StrawberryEmployeeId, StrawberryEmployee> builder = IndexedStoreBuilder.<StrawberryEmployeeId, StrawberryEmployee>builder()
        .name(StrawberryEmployee.class.getName())
        .kClass(StrawberryEmployeeId.class)
        .vClass(StrawberryEmployee.class)
        .indices(StrawberryEmployee.INDICES)
        .identityGetter(StrawberryEmployee::identity)
        .build();

    return (IndexedStoreImpl<StrawberryEmployeeId, StrawberryEmployee>) builder.build();
  }

  public IndexedStoreImpl<StrawberryTeamId, StrawberryTeam> buildStrawberryTeamStore() {
    IndexedStoreBuilder<StrawberryTeamId, StrawberryTeam> builder = IndexedStoreBuilder.<StrawberryTeamId, StrawberryTeam>builder()
        .name(StrawberryTeam.class.getName())
        .kClass(StrawberryTeamId.class)
        .vClass(StrawberryTeam.class)
        .indices(StrawberryTeam.INDICES)
        .identityGetter(StrawberryTeam::identity)
        .build();

    return (IndexedStoreImpl<StrawberryTeamId, StrawberryTeam>) builder.build();
  }
}
