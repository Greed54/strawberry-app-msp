package com.strawberry.app.core.context.workday;

import static com.googlecode.cqengine.query.QueryFactory.attribute;

import com.google.common.collect.ImmutableSet;
import com.googlecode.cqengine.attribute.support.AbstractAttribute;
import com.strawberry.app.common.cqengine.ProjectionIndex;
import com.strawberry.app.common.projection.State;
import com.strawberry.app.common.property.context.HasRemoved;
import com.strawberry.app.common.property.context.created.HasCreatedAt;
import com.strawberry.app.common.property.context.modified.HasOptionalModified;
import com.strawberry.app.core.context.workday.identities.StrawberryWorkDayId;
import com.strawberry.app.core.context.workday.properties.AllStrawberryWorkDayProps;
import com.strawberry.app.core.context.workday.properties.HasStrawberryWorkDayId;
import java.time.Instant;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryWorkDay extends State<StrawberryWorkDayId>, HasStrawberryWorkDayId, AllStrawberryWorkDayProps, HasCreatedAt,
    HasOptionalModified, HasRemoved {

  class Attributes {

    public static AbstractAttribute<StrawberryWorkDay, StrawberryWorkDayId> WORK_DAY_ID = attribute(
        StrawberryWorkDay.class, StrawberryWorkDayId.class, "identity", StrawberryWorkDay::identity);
    public static AbstractAttribute<StrawberryWorkDay, Instant> WORK_DAY_DATE = attribute(
        StrawberryWorkDay.class, Instant.class, "date", StrawberryWorkDay::date);
  }

  ImmutableSet<ProjectionIndex<StrawberryWorkDay>> INDICES = ImmutableSet.of(
      ProjectionIndex.hash(Attributes.WORK_DAY_ID),
      ProjectionIndex.hash(Attributes.WORK_DAY_DATE)
  );
}
