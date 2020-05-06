package com.strawberry.app.read.application.store;

import com.strawberry.app.common.cqengine.indexedstore.IndexedProjectionStoreBuilder;
import com.strawberry.app.common.cqengine.indexedstore.IndexedStoreImpl;
import com.strawberry.app.common.topology.AbstractTopology;
import com.strawberry.app.core.context.box.identities.StrawberryBoxId;
import com.strawberry.app.core.context.box.projecton.IStrawberryBoxProjectionEvent;
import com.strawberry.app.core.context.box.projecton.StrawberryBoxProjectionEvent;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.projection.IStrawberryEmployeeProjectionEvent;
import com.strawberry.app.core.context.employee.projection.StrawberryEmployeeProjectionEvent;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.team.projection.IStrawberryTeamProjectionEvent;
import com.strawberry.app.core.context.team.projection.StrawberryTeamProjectionEvent;
import com.strawberry.app.core.context.workday.identities.StrawberryWorkDayId;
import com.strawberry.app.core.context.workday.projection.IStrawberryWorkDayProjectionEvent;
import com.strawberry.app.core.context.workday.projection.StrawberryWorkDayProjectionEvent;
import java.util.List;
import java.util.stream.Collectors;
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

  public static IndexedStoreImpl<StrawberryWorkDayId, StrawberryWorkDayProjectionEvent> buildStrawberryWorkDayProjectionStore() {
    IndexedProjectionStoreBuilder<StrawberryWorkDayId, StrawberryWorkDayProjectionEvent> builder = IndexedProjectionStoreBuilder.<StrawberryWorkDayId, StrawberryWorkDayProjectionEvent>builder()
        .name(StrawberryWorkDayProjectionEvent.class.getName())
        .eventStream(IStrawberryWorkDayProjectionEvent.eventStream())
        .indices(StrawberryWorkDayProjectionEvent.INDICES)
        .identityGetter(StrawberryWorkDayProjectionEvent::identity)
        .build();

    return (IndexedStoreImpl<StrawberryWorkDayId, StrawberryWorkDayProjectionEvent>) builder.build();
  }

  public static IndexedStoreImpl<StrawberryBoxId, StrawberryBoxProjectionEvent> buildStrawberryBoxProjectionStore() {
    IndexedProjectionStoreBuilder<StrawberryBoxId, StrawberryBoxProjectionEvent> builder = IndexedProjectionStoreBuilder.<StrawberryBoxId, StrawberryBoxProjectionEvent>builder()
        .name(StrawberryBoxProjectionEvent.class.getName())
        .eventStream(IStrawberryBoxProjectionEvent.eventStream())
        .indices(StrawberryBoxProjectionEvent.INDICES)
        .identityGetter(StrawberryBoxProjectionEvent::identity)
        .build();

    return (IndexedStoreImpl<StrawberryBoxId, StrawberryBoxProjectionEvent>) builder.build();
  }

  public static List<IndexedStoreImpl> buildProjectionStores(List<AbstractTopology> topologies) {
    return topologies.stream()
        .map(topology -> (IndexedStoreImpl) IndexedProjectionStoreBuilder.builder()
            .name(topology.topologyName())
            .eventStream(topology.eventStream())
            .indices(topology.indices())
            .identityGetter(topology.identityGetter())
            .build()
            .build())
        .collect(Collectors.toList());
  }
}
