package com.strawberry.app.core.application;


import com.strawberry.app.core.context.employee.command.AddStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeNoteCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeRoleCommand;
import com.strawberry.app.core.context.employee.service.StrawberryEmployeeCommandServiceImpl;
import java.util.concurrent.CompletableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

  private final StrawberryEmployeeCommandServiceImpl commandService;

  public EmployeeController(StrawberryEmployeeCommandServiceImpl commandService) {
    this.commandService = commandService;
  }

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
