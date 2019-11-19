package com.strawberry.app.core.context.cqrscommon.projection;

import com.strawberry.app.core.context.cqrscommon.DomainObject;
import com.strawberry.app.core.context.cqrscommon.Identity;

public interface Projection<K extends Identity<?>> extends DomainObject<K> {

}
