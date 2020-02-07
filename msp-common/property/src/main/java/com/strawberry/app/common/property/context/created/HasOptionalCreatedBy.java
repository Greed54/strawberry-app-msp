package com.strawberry.app.common.property.context.created;

import com.strawberry.app.common.property.context.identity.PersonId;
import javax.annotation.Nullable;

public interface HasOptionalCreatedBy {

  @Nullable
  PersonId createdBy();

}
