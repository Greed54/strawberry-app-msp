package com.strawberry.app.core.context.employee.service;

import com.strawberry.app.core.context.employee.command.AddStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeNoteCommand;
import com.strawberry.app.core.context.employee.command.AmendStrawberryEmployeeRoleCommand;
import java.util.concurrent.CompletableFuture;

public interface StrawberryEmployeeCommandService {

  CompletableFuture<String> createEmployee(AddStrawberryEmployeeCommand addStrawberryEmployeeCommand);
  CompletableFuture<String> amendEmployee(AmendStrawberryEmployeeCommand amendStrawberryEmployeeCommand);
  CompletableFuture<String> amendEmployeeNote(AmendStrawberryEmployeeNoteCommand amendStrawberryEmployeeNoteCommand);
  CompletableFuture<String> amendEmployeeRole(AmendStrawberryEmployeeRoleCommand amendStrawberryEmployeeRoleCommand);

}
