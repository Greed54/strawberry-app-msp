package com.strawberry.app.core.context.employee;

import com.strawberry.app.core.context.common.property.context.created.HasCreatedAt;
import com.strawberry.app.core.context.common.property.context.created.HasOptionalCreatedBy;
import com.strawberry.app.core.context.common.property.context.modified.HasOptionalModified;
import com.strawberry.app.core.context.cqrscommon.projection.State;
import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import com.strawberry.app.core.context.employee.properties.AllStrawberryEmployeeProps;
import com.strawberry.app.core.context.employee.properties.HasStrawberryEmployeeId;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryEmployee extends State<StrawberryEmployeeId>, HasStrawberryEmployeeId, AllStrawberryEmployeeProps, HasCreatedAt,
    HasOptionalCreatedBy, HasOptionalModified {

}
