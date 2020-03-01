package com.strawberry.app.core.context.employee.projection;

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
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.properties.AllStrawberryEmployeeProps;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeId;
import java.util.Set;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryEmployeeProjectionEvent extends ProjectionEvent<StrawberryEmployeeId>, HasStrawberryEmployeeId,
    AllStrawberryEmployeeProps, HasRemoved,
    HasCreatedAt, HasOptionalCreatedBy, HasOptionalModified {

  static ProjectionEventStream<StrawberryEmployeeId, StrawberryEmployeeProjectionEvent> eventStream() {
    return new ProjectionEventStream<>() {
      @Override
      public Class<StrawberryEmployeeId> getKeyClass() {
        return StrawberryEmployeeId.class;
      }

      @Override
      public Class<StrawberryEmployeeProjectionEvent> getValueClass() {
        return StrawberryEmployeeProjectionEvent.class;
      }
    };
  }

  class Attributes {
    public static AbstractAttribute<StrawberryEmployeeProjectionEvent, StrawberryEmployeeId> EMPLOYEE_ID = attribute(
        StrawberryEmployeeProjectionEvent.class, StrawberryEmployeeId.class, "identity", StrawberryEmployeeProjectionEvent::identity);
  }

  Set<ProjectionIndex<StrawberryEmployeeProjectionEvent>> INDICES = ImmutableSet.of(
      ProjectionIndex.hash(Attributes.EMPLOYEE_ID)
  );
}
