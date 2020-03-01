package com.strawberry.app.core.context.workday.utils;

import com.strawberry.app.core.context.workday.service.StrawberryWorkDayService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class StrawberryWorkDayValidator {

  StrawberryWorkDayService workDayService;


}
