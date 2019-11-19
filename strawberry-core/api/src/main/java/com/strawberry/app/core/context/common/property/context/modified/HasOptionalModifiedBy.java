package com.strawberry.app.core.context.common.property.context.modified;

import com.strawberry.app.core.context.common.property.context.identity.PersonId;
import javax.annotation.Nullable;

public interface HasOptionalModifiedBy {

  @Nullable
  PersonId modifiedBy();

}
