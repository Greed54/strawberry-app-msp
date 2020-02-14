package com.strawberry.app.restapi.service.employee;

import com.strawberry.app.core.context.employee.command.AddStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeNoteCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeRoleCommand;
import reactor.core.publisher.Mono;

public interface StrawberryEmployeeCommandService {

  Mono<String> createEmployee(AddStrawberryEmployeeCommand addStrawberryEmployeeCommand);

  Mono<String> amendEmployee(AmendStrawberryEmployeeCommand amendStrawberryEmployeeCommand);

  Mono<String> amendEmployeeNote(AmendStrawberryEmployeeNoteCommand amendStrawberryEmployeeNoteCommand);

  Mono<String> amendEmployeeRole(AmendStrawberryEmployeeRoleCommand amendStrawberryEmployeeRoleCommand);

}
