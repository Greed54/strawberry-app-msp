package com.strawberry.app.core.context.workday.command;

import com.strawberry.app.common.property.context.modified.HasModified;
import com.strawberry.app.core.context.workday.properties.HasStrawberryWorkDayPricePerKilo;
import com.strawberry.app.core.context.workday.properties.HasStrawberryWorkDayTareWeight;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IAmendStrawberryWorkDayCommand extends StrawberryWorkDayCommand,
    HasStrawberryWorkDayPricePerKilo,
    HasStrawberryWorkDayTareWeight,
    HasModified {

}
