package com.strawberry.app.core.context.team.properties;

import com.strawberry.app.core.context.employee.identities.StrawberryEmployeeId;
import org.jetbrains.annotations.Nullable;

public interface HasStrawberryTeamLeadId {

  @Nullable
  StrawberryEmployeeId teamLeadId();
}
