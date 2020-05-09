package com.strawberry.app.core.context.person;

import com.strawberry.app.common.projection.State;
import com.strawberry.app.common.property.context.HasRemoved;
import com.strawberry.app.common.property.context.created.HasCreatedAt;
import com.strawberry.app.common.property.context.modified.HasOptionalModifiedAt;
import com.strawberry.app.core.context.person.identities.StrawberryPersonId;
import com.strawberry.app.core.context.person.properties.AllStrawberryPersonProps;
import com.strawberry.app.core.context.person.properties.HasStrawberryPersonId;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryPerson extends State<StrawberryPersonId>, HasStrawberryPersonId, AllStrawberryPersonProps, HasRemoved, HasCreatedAt,
    HasOptionalModifiedAt {

}
