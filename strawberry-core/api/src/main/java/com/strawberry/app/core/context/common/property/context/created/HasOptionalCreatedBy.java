package com.strawberry.app.core.context.common.property.context.created;

import com.strawberry.app.core.context.common.property.context.identity.PersonId;
import javax.annotation.Nullable;

public interface HasOptionalCreatedBy {

  @Nullable
  PersonId createdBy();

}
