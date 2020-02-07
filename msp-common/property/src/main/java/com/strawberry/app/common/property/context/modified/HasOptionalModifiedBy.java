package com.strawberry.app.common.property.context.modified;

import com.strawberry.app.common.property.context.identity.PersonId;
import javax.annotation.Nullable;

public interface HasOptionalModifiedBy {

  @Nullable
  PersonId modifiedBy();

}
