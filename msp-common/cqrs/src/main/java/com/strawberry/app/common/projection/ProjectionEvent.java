package com.strawberry.app.common.projection;

import com.strawberry.app.common.event.Event;
import com.strawberry.app.common.Identity;

public interface ProjectionEvent<K extends Identity<?>> extends Event<K> {

}
