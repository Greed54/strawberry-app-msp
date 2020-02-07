package com.strawberry.app.common.property.context.modified;

import java.time.Instant;
import javax.annotation.Nullable;

public interface HasOptionalModifiedAt {

  @Nullable
  Instant modifiedAt();

}
