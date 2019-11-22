package com.strawberry.app.core.context.cqrscommon.event;

import com.strawberry.app.core.context.cqrscommon.Identity;

public interface BusinessEvent<K extends Identity<?>> extends Event<K> {

}
