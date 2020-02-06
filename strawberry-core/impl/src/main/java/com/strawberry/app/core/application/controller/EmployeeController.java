package com.strawberry.app.core.application.controller;


import com.strawberry.app.core.context.employee.command.AddStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeNoteCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeRoleCommand;
import com.strawberry.app.core.context.employee.service.StrawberryEmployeeCommandServiceImpl;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class EmployeeController {

  StrawberryEmployeeCommandServiceImpl commandService;

  @PostMapping("/api/addEmployee")
  public CompletableFuture<String> addEmployee(@RequestBody AddStrawberryEmployeeCommand addStrawberryEmployeeCommand) {
    return commandService.createEmployee(addStrawberryEmployeeCommand);
  }

  @PostMapping("/api/amendEmployee")
  public CompletableFuture<String> amendEmployee(@RequestBody AmendStrawberryEmployeeCommand amendStrawberryEmployeeCommand) {
    return commandService.amendEmployee(amendStrawberryEmployeeCommand);
  }

  @PostMapping("/api/amendEmployeeRole")
  public CompletableFuture<String> amendEmployeeRole(@RequestBody AmendStrawberryEmployeeRoleCommand amendStrawberryEmployeeRoleCommand) {
    return commandService.amendEmployeeRole(amendStrawberryEmployeeRoleCommand);
  }

  @PostMapping("/api/amendEmployeeNote")
  public CompletableFuture<String> amendEmployeeNote(@RequestBody AmendStrawberryEmployeeNoteCommand amendStrawberryEmployeeNoteCommand) {
    return commandService.amendEmployeeNote(amendStrawberryEmployeeNoteCommand);
  }
}
