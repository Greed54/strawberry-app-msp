package com.strawberry.app.common.event.business;

import com.strawberry.app.common.Identity;
import com.strawberry.app.common.event.Event;

public interface BusinessEvent<K extends Identity<?>> extends Event<K> {

}
