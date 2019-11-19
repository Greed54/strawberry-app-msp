package com.strawberry.app.core.context.employee.properties;

import com.strawberry.app.core.context.enums.EmployeeRole;
import javax.annotation.Nullable;
import org.immutables.value.Value.Default;

public interface HasStrawberryEmployeeRole {

  @Default
  @Nullable
  default EmployeeRole employeeRole() {
    return EmployeeRole.PICKER;
  }
}
