package com.strawberry.app.common.event;

import com.strawberry.app.common.Identity;

public interface BusinessEvent<K extends Identity<?>> extends Event<K> {

}
