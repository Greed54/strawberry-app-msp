package com.strawberry.app.core.context.common.utils.read.event;

import com.strawberry.app.core.context.common.property.context.created.HasCreatedAt;
import com.strawberry.app.core.context.common.property.context.created.HasCreatedBy;
import com.strawberry.app.core.context.common.property.context.created.HasOptionalCreatedBy;
import com.strawberry.app.core.context.common.property.context.identity.PersonId;
import com.strawberry.app.core.context.common.property.context.modified.HasModified;
import com.strawberry.app.core.context.common.property.context.modified.HasModifiedAt;
import com.strawberry.app.core.context.common.property.context.modified.HasModifiedBy;
import com.strawberry.app.core.context.common.property.context.modified.HasOptionalModifiedAt;
import com.strawberry.app.core.context.common.property.context.modified.HasOptionalModifiedBy;
import com.strawberry.app.core.context.cqrscommon.DomainObject;
import java.time.Instant;

public class Util {

  public static Instant getModifiedAt(DomainObject domainObject) {
    if (domainObject instanceof HasModifiedAt) {
      return ((HasModified) domainObject).modifiedAt();
    }
    if (domainObject instanceof HasOptionalModifiedAt) {
      return ((HasOptionalModifiedAt) domainObject).modifiedAt();
    }
    if (domainObject instanceof HasCreatedAt) {
      return ((HasCreatedAt) domainObject).createdAt();
    }
    return Instant.now();
  }

  public static PersonId getModifiedBy(DomainObject domainObject) {
    if (domainObject instanceof HasModifiedBy) {
      return ((HasModifiedBy) domainObject).modifiedBy();
    }
    if (domainObject instanceof HasOptionalModifiedBy) {
      return ((HasOptionalModifiedBy) domainObject).modifiedBy();
    }
    if (domainObject instanceof HasCreatedBy) {
      return ((HasCreatedBy) domainObject).createdBy();
    }
    if (domainObject instanceof HasOptionalCreatedBy) {
      return ((HasOptionalCreatedBy) domainObject).createdBy();
    }
    return null;
  }
}
