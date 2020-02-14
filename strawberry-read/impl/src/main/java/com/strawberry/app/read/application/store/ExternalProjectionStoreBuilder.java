package com.strawberry.app.read.application.store;

import com.strawberry.app.common.cqengine.indexedstore.IndexedProjectionStoreBuilder;
import com.strawberry.app.common.cqengine.indexedstore.IndexedStoreImpl;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.projection.IStrawberryEmployeeProjectionEvent;
import com.strawberry.app.core.context.employee.projection.StrawberryEmployeeProjectionEvent;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.team.projection.IStrawberryTeamProjectionEvent;
import com.strawberry.app.core.context.team.projection.StrawberryTeamProjectionEvent;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExternalProjectionStoreBuilder {

  public static IndexedStoreImpl<StrawberryEmployeeId, StrawberryEmployeeProjectionEvent> buildStrawberryEmployeeProjectionStore() {
    IndexedProjectionStoreBuilder<StrawberryEmployeeId, StrawberryEmployeeProjectionEvent> builder = IndexedProjectionStoreBuilder.<StrawberryEmployeeId, StrawberryEmployeeProjectionEvent>builder()
        .name(StrawberryEmployeeProjectionEvent.class.getName())
        .eventStream(IStrawberryEmployeeProjectionEvent.eventStream())
        .indices(StrawberryEmployeeProjectionEvent.INDICES)
        .identityGetter(StrawberryEmployeeProjectionEvent::identity)
        .build();

    return (IndexedStoreImpl<StrawberryEmployeeId, StrawberryEmployeeProjectionEvent>) builder.build();
  }

  public static IndexedStoreImpl<StrawberryTeamId, StrawberryTeamProjectionEvent> buildStrawberryTeamProjectionStore() {
    IndexedProjectionStoreBuilder<StrawberryTeamId, StrawberryTeamProjectionEvent> builder = IndexedProjectionStoreBuilder.<StrawberryTeamId, StrawberryTeamProjectionEvent>builder()
        .name(StrawberryTeamProjectionEvent.class.getName())
        .eventStream(IStrawberryTeamProjectionEvent.eventStream())
        .indices(StrawberryTeamProjectionEvent.INDICES)
        .identityGetter(StrawberryTeamProjectionEvent::identity)
        .build();

    return (IndexedStoreImpl<StrawberryTeamId, StrawberryTeamProjectionEvent>) builder.build();
  }
}
