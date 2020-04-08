package com.strawberry.app.core.context.box.event;

import com.strawberry.app.common.event.BusinessEvent;
import com.strawberry.app.core.context.box.identities.StrawberryBoxId;
import com.strawberry.app.core.context.box.properties.HasStrawberryBoxId;

public interface StrawberryBoxEvent extends BusinessEvent<StrawberryBoxId>, HasStrawberryBoxId {

}
