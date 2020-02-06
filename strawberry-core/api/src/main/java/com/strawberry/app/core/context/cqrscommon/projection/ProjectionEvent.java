package com.strawberry.app.core.context.cqrscommon.projection;

import com.strawberry.app.core.context.cqrscommon.event.Event;
import com.strawberry.app.core.context.cqrscommon.Identity;

public interface ProjectionEvent<K extends Identity<?>> extends Event<K> {

}
