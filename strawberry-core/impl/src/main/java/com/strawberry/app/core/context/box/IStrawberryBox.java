package com.strawberry.app.core.context.box;

import static com.googlecode.cqengine.query.QueryFactory.attribute;

import com.google.common.collect.ImmutableSet;
import com.googlecode.cqengine.attribute.support.AbstractAttribute;
import com.strawberry.app.common.cqengine.ProjectionIndex;
import com.strawberry.app.common.projection.State;
import com.strawberry.app.common.property.context.HasRemoved;
import com.strawberry.app.common.property.context.created.HasCreatedAt;
import com.strawberry.app.common.property.context.modified.HasOptionalModified;
import com.strawberry.app.core.context.box.identities.StrawberryBoxId;
import com.strawberry.app.core.context.box.properties.AllStrawberryBoxProps;
import com.strawberry.app.core.context.box.properties.HasStrawberryBoxId;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryBox extends State<StrawberryBoxId>, HasStrawberryBoxId, AllStrawberryBoxProps, HasCreatedAt, HasOptionalModified,
    HasRemoved {

  class Attributes {

    public static AbstractAttribute<StrawberryBox, StrawberryBoxId> BOX_ID = attribute(
        StrawberryBox.class, StrawberryBoxId.class, "identity", StrawberryBox::identity);
  }

  ImmutableSet<ProjectionIndex<StrawberryBox>> INDICES = ImmutableSet.of(
      ProjectionIndex.hash(Attributes.BOX_ID)
  );
}
