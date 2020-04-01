package com.strawberry.app.core.context.workday.event;

import com.strawberry.app.common.event.BusinessEvent;
import com.strawberry.app.core.context.workday.identities.StrawberryWorkDayId;
import com.strawberry.app.core.context.workday.properties.HasStrawberryWorkDayId;

public interface StrawberryWorkDayEvent extends BusinessEvent<StrawberryWorkDayId>, HasStrawberryWorkDayId {

}
