package com.strawberry.app.core.context.team.projection;

import static com.googlecode.cqengine.query.QueryFactory.attribute;

import com.google.common.collect.ImmutableSet;
import com.googlecode.cqengine.attribute.support.AbstractAttribute;
import com.strawberry.app.common.cqengine.ProjectionIndex;
import com.strawberry.app.common.projection.ProjectionEvent;
import com.strawberry.app.common.projection.ProjectionEventStream;
import com.strawberry.app.common.property.context.HasRemoved;
import com.strawberry.app.common.property.context.created.HasCreatedAt;
import com.strawberry.app.common.property.context.created.HasOptionalCreatedBy;
import com.strawberry.app.common.property.context.modified.HasOptionalModified;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.team.properties.BaseStrawberryTeamProps;
import com.strawberry.app.core.context.team.properties.HasStrawberryTeamId;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryTeamProjectionEvent extends ProjectionEvent<StrawberryTeamId>, HasStrawberryTeamId, BaseStrawberryTeamProps, HasRemoved,
    HasCreatedAt,
    HasOptionalCreatedBy, HasOptionalModified {

  static ProjectionEventStream<StrawberryTeamId, StrawberryTeamProjectionEvent> eventStream() {
    return new ProjectionEventStream<>() {
      @Override
      public Class<StrawberryTeamId> getKeyClass() {
        return StrawberryTeamId.class;
      }

      @Override
      public Class<StrawberryTeamProjectionEvent> getValueClass() {
        return StrawberryTeamProjectionEvent.class;
      }
    };
  }

  class Attributes {

    public static AbstractAttribute<StrawberryTeamProjectionEvent, StrawberryTeamId> TEAM_ID = attribute(
        StrawberryTeamProjectionEvent.class, StrawberryTeamId.class, "identity", StrawberryTeamProjectionEvent::identity);

  }

  ImmutableSet<ProjectionIndex<StrawberryTeamProjectionEvent>> INDICES = ImmutableSet.of(
      ProjectionIndex.hash(Attributes.TEAM_ID)
  );
}
