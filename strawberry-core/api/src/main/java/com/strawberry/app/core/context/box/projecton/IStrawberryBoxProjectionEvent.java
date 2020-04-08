package com.strawberry.app.core.context.box.projecton;

import static com.googlecode.cqengine.query.QueryFactory.attribute;

import com.google.common.collect.ImmutableSet;
import com.googlecode.cqengine.attribute.support.AbstractAttribute;
import com.strawberry.app.common.cqengine.ProjectionIndex;
import com.strawberry.app.common.projection.ProjectionEvent;
import com.strawberry.app.common.projection.ProjectionEventStream;
import com.strawberry.app.common.property.context.HasRemoved;
import com.strawberry.app.common.property.context.created.HasCreatedAt;
import com.strawberry.app.common.property.context.modified.HasOptionalModified;
import com.strawberry.app.core.context.box.identities.StrawberryBoxId;
import com.strawberry.app.core.context.box.properties.AllStrawberryBoxProps;
import com.strawberry.app.core.context.box.properties.HasStrawberryBoxId;
import java.util.Set;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryBoxProjectionEvent extends ProjectionEvent<StrawberryBoxId>, HasStrawberryBoxId, AllStrawberryBoxProps,
    HasRemoved, HasCreatedAt, HasOptionalModified {

  static ProjectionEventStream<StrawberryBoxId, StrawberryBoxProjectionEvent> eventStream() {
    return new ProjectionEventStream<>() {
      @Override
      public Class<StrawberryBoxId> getKeyClass() {
        return StrawberryBoxId.class;
      }

      @Override
      public Class<StrawberryBoxProjectionEvent> getValueClass() {
        return StrawberryBoxProjectionEvent.class;
      }
    };
  }

  class Attributes {

    public static AbstractAttribute<StrawberryBoxProjectionEvent, StrawberryBoxId> BOX_ID = attribute(
        StrawberryBoxProjectionEvent.class, StrawberryBoxId.class, "identity", StrawberryBoxProjectionEvent::identity);
  }

  Set<ProjectionIndex<StrawberryBoxProjectionEvent>> INDICES = ImmutableSet.of(
      ProjectionIndex.hash(Attributes.BOX_ID)
  );
}
