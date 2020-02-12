package com.strawberry.app.restapi.rest;


import com.strawberry.app.core.context.employee.command.AddStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeNoteCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeRoleCommand;
import com.strawberry.app.restapi.service.employee.StrawberryEmployeeCommandServiceImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class EmployeeController {

  StrawberryEmployeeCommandServiceImpl commandService;

  @PostMapping("/api/addEmployee")
  public Mono<String> addEmployee(@RequestBody AddStrawberryEmployeeCommand addStrawberryEmployeeCommand) {
    return commandService.createEmployee(addStrawberryEmployeeCommand);
  }

  @PostMapping("/api/amendEmployee")
  public Mono<String> amendEmployee(@RequestBody AmendStrawberryEmployeeCommand amendStrawberryEmployeeCommand) {
    return commandService.amendEmployee(amendStrawberryEmployeeCommand);
  }

  @PostMapping("/api/amendEmployeeRole")
  public Mono<String> amendEmployeeRole(@RequestBody AmendStrawberryEmployeeRoleCommand amendStrawberryEmployeeRoleCommand) {
    return commandService.amendEmployeeRole(amendStrawberryEmployeeRoleCommand);
  }

  @PostMapping("/api/amendEmployeeNote")
  public Mono<String> amendEmployeeNote(@RequestBody AmendStrawberryEmployeeNoteCommand amendStrawberryEmployeeNoteCommand) {
    return commandService.amendEmployeeNote(amendStrawberryEmployeeNoteCommand);
  }
}
