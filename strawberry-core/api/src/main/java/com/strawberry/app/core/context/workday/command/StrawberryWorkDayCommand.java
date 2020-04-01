package com.strawberry.app.core.context.workday.command;

import com.strawberry.app.common.Command;
import com.strawberry.app.core.context.workday.identities.StrawberryWorkDayId;
import com.strawberry.app.core.context.workday.properties.HasStrawberryWorkDayId;

public interface StrawberryWorkDayCommand extends Command<StrawberryWorkDayId>, HasStrawberryWorkDayId {

}
