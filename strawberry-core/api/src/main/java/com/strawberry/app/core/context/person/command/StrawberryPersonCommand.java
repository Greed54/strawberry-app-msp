package com.strawberry.app.core.context.person.command;

import com.strawberry.app.common.Command;
import com.strawberry.app.core.context.person.identities.StrawberryPersonId;
import com.strawberry.app.core.context.person.properties.HasStrawberryPersonId;

public interface StrawberryPersonCommand extends Command<StrawberryPersonId>, HasStrawberryPersonId {

}
