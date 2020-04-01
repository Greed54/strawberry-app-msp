package com.strawberry.app.restapi.service.workday;

import com.strawberry.app.core.context.workday.command.AmendStrawberryWorkDayCommand;
import reactor.core.publisher.Mono;

public interface IStrawberryWorkDayCommandService {

  Mono<String> amendWorkDay(AmendStrawberryWorkDayCommand amendStrawberryWorkDayCommand);

}
