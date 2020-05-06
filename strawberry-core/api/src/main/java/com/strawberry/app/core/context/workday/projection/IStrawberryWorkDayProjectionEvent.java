package com.strawberry.app.core.context.workday.projection;

import static com.googlecode.cqengine.query.QueryFactory.attribute;

import com.google.common.collect.ImmutableSet;
import com.googlecode.cqengine.attribute.support.AbstractAttribute;
import com.strawberry.app.common.cqengine.ProjectionIndex;
import com.strawberry.app.common.projection.ProjectionEvent;
import com.strawberry.app.common.projection.ProjectionEventStream;
import com.strawberry.app.common.property.context.HasRemoved;
import com.strawberry.app.common.property.context.created.HasCreatedAt;
import com.strawberry.app.common.property.context.modified.HasOptionalModified;
import com.strawberry.app.core.context.workday.identities.StrawberryWorkDayId;
import com.strawberry.app.core.context.workday.properties.AllStrawberryWorkDayProps;
import com.strawberry.app.core.context.workday.properties.HasStrawberryWorkDayId;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryWorkDayProjectionEvent extends ProjectionEvent<StrawberryWorkDayId>, HasStrawberryWorkDayId,
    AllStrawberryWorkDayProps, HasRemoved,
    HasCreatedAt, HasOptionalModified {

  static ProjectionEventStream<StrawberryWorkDayId, StrawberryWorkDayProjectionEvent> eventStream() {
    return new ProjectionEventStream<>() {
      @Override
      public Class<StrawberryWorkDayId> getKeyClass() {
        return StrawberryWorkDayId.class;
      }

      @Override
      public Class<StrawberryWorkDayProjectionEvent> getValueClass() {
        return StrawberryWorkDayProjectionEvent.class;
      }
    };
  }

  class Attributes {

    public static AbstractAttribute<StrawberryWorkDayProjectionEvent, StrawberryWorkDayId> WORK_DAY_ID = attribute(
        StrawberryWorkDayProjectionEvent.class, StrawberryWorkDayId.class, "identity", StrawberryWorkDayProjectionEvent::identity);
  }

  ImmutableSet<ProjectionIndex<StrawberryWorkDayProjectionEvent>> INDICES = ImmutableSet.of(
      ProjectionIndex.hash(Attributes.WORK_DAY_ID)
  );
}
