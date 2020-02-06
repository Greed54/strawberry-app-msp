package com.strawberry.app.core.context.cqrscommon.event;

import com.strawberry.app.core.context.cqrscommon.DomainObject;
import com.strawberry.app.core.context.cqrscommon.Identity;

public interface Event<K extends Identity<?>> extends DomainObject<K> {

}
