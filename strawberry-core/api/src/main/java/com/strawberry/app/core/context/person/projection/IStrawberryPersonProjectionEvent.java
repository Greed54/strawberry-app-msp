package com.strawberry.app.core.context.person.projection;

import static com.googlecode.cqengine.query.QueryFactory.attribute;

import com.google.common.collect.ImmutableSet;
import com.googlecode.cqengine.attribute.support.AbstractAttribute;
import com.strawberry.app.common.cqengine.ProjectionIndex;
import com.strawberry.app.common.projection.ProjectionEvent;
import com.strawberry.app.common.projection.ProjectionEventStream;
import com.strawberry.app.common.property.context.HasRemoved;
import com.strawberry.app.common.property.context.created.HasCreatedAt;
import com.strawberry.app.common.property.context.modified.HasOptionalModifiedAt;
import com.strawberry.app.core.context.person.identities.StrawberryPersonId;
import com.strawberry.app.core.context.person.properties.AllStrawberryPersonProps;
import com.strawberry.app.core.context.person.properties.HasStrawberryPersonId;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryPersonProjectionEvent extends ProjectionEvent<StrawberryPersonId>, HasStrawberryPersonId, AllStrawberryPersonProps,
    HasRemoved,
    HasCreatedAt, HasOptionalModifiedAt {

  static ProjectionEventStream<StrawberryPersonId, StrawberryPersonProjectionEvent> eventStream() {
    return new ProjectionEventStream<>() {
      @Override
      public Class<StrawberryPersonId> getKeyClass() {
        return StrawberryPersonId.class;
      }

      @Override
      public Class<StrawberryPersonProjectionEvent> getValueClass() {
        return StrawberryPersonProjectionEvent.class;
      }
    };
  }

  class Attributes {

    public static AbstractAttribute<StrawberryPersonProjectionEvent, StrawberryPersonId> PERSON_ID = attribute(
        StrawberryPersonProjectionEvent.class, StrawberryPersonId.class, "identity", StrawberryPersonProjectionEvent::identity);
  }

  ImmutableSet<ProjectionIndex<StrawberryPersonProjectionEvent>> INDICES = ImmutableSet.of(
      ProjectionIndex.hash(Attributes.PERSON_ID)
  );
}
