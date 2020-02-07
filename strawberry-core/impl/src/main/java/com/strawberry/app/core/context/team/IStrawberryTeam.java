package com.strawberry.app.core.context.team;

import static com.googlecode.cqengine.query.QueryFactory.attribute;

import com.google.common.collect.ImmutableSet;
import com.googlecode.cqengine.attribute.support.AbstractAttribute;
import com.strawberry.app.common.cqengine.ProjectionIndex;
import com.strawberry.app.common.property.context.HasRemoved;
import com.strawberry.app.common.property.context.created.HasCreatedAt;
import com.strawberry.app.common.property.context.created.HasOptionalCreatedBy;
import com.strawberry.app.common.property.context.modified.HasOptionalModified;
import com.strawberry.app.common.projection.State;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.team.properties.BaseStrawberryTeamProps;
import com.strawberry.app.core.context.team.properties.HasStrawberryTeamId;
import java.util.Set;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryTeam extends State<StrawberryTeamId>, HasStrawberryTeamId, BaseStrawberryTeamProps, HasRemoved, HasCreatedAt,
    HasOptionalCreatedBy, HasOptionalModified {

  class Attributes {

    public static AbstractAttribute<StrawberryTeam, StrawberryTeamId> TEAM_ID = attribute(
        StrawberryTeam.class, StrawberryTeamId.class, "identity", StrawberryTeam::identity);
  }

  Set<ProjectionIndex<StrawberryTeam>> INDICES = ImmutableSet.of(
      ProjectionIndex.hash(Attributes.TEAM_ID)
  );
}
