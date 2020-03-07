package com.strawberry.app.core.context.box.command;

import com.strawberry.app.common.Command;
import com.strawberry.app.core.context.box.identities.StrawberryBoxId;
import com.strawberry.app.core.context.box.properties.HasStrawberryBoxId;

public interface StrawberryBoxCommand extends Command<StrawberryBoxId>, HasStrawberryBoxId {

}
