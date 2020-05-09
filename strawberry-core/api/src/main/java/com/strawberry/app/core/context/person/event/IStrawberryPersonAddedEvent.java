package com.strawberry.app.core.context.person.event;

import com.strawberry.app.common.property.context.created.HasCreatedAt;
import com.strawberry.app.core.context.person.properties.AllStrawberryPersonProps;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IStrawberryPersonAddedEvent extends StrawberryPersonEvent, AllStrawberryPersonProps, HasCreatedAt {

}
