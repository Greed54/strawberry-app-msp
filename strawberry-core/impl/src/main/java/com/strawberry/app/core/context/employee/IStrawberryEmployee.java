package com.strawberry.app.core.context.employee;

import static com.googlecode.cqengine.query.QueryFactory.attribute;
import static com.googlecode.cqengine.query.QueryFactory.nullableAttribute;

import com.google.common.collect.ImmutableSet;
import com.googlecode.cqengine.attribute.support.AbstractAttribute;
import com.strawberry.app.core.context.common.cqengine.ProjectionIndex;
import com.strawberry.app.core.context.common.property.context.HasRemoved;
import com.strawberry.app.core.context.common.property.context.created.HasCreatedAt;
import com.strawberry.app.core.context.common.property.context.created.HasOptionalCreatedBy;
import com.strawberry.app.core.context.common.property.context.identity.card.CardId;
import com.strawberry.app.core.context.common.property.context.modified.HasOptionalModified;
import com.strawberry.app.core.context.cqrscommon.projection.State;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.properties.AllStrawberryEmployeeProps;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeId;
import com.strawberry.app.core.context.enums.EmployeeRole;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import java.util.Set;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryEmployee extends State<StrawberryEmployeeId>, HasStrawberryEmployeeId, AllStrawberryEmployeeProps, HasCreatedAt,
    HasOptionalCreatedBy, HasOptionalModified, HasRemoved {

  class Attributes {

    public static AbstractAttribute<StrawberryEmployee, StrawberryEmployeeId> EMPLOYEE_ID = attribute(
        StrawberryEmployee.class, StrawberryEmployeeId.class, "identity", StrawberryEmployee::identity);

    public static AbstractAttribute<StrawberryEmployee, StrawberryTeamId> TEAM_ID = attribute(StrawberryEmployee.class,
        StrawberryTeamId.class, "teamId", StrawberryEmployee::teamId);

    public static AbstractAttribute<StrawberryEmployee, EmployeeRole> EMPLOYEE_ROLE = nullableAttribute(
        StrawberryEmployee.class, EmployeeRole.class, "employeeRole", StrawberryEmployee::employeeRole);

    public static AbstractAttribute<StrawberryEmployee, CardId> CARD_ID = attribute(StrawberryEmployee.class, CardId.class, "cardId",
        StrawberryEmployee::cardId);
  }

  Set<ProjectionIndex<StrawberryEmployee>> INDICES = ImmutableSet.of(
      ProjectionIndex.hash(Attributes.EMPLOYEE_ID),
      ProjectionIndex.hash(Attributes.TEAM_ID),
      ProjectionIndex.hash(Attributes.EMPLOYEE_ROLE),
      ProjectionIndex.hash(Attributes.CARD_ID)
  );
}
