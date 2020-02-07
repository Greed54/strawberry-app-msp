package com.strawberry.app.common.event;

import com.strawberry.app.common.DomainObject;
import com.strawberry.app.common.Identity;

public interface Event<K extends Identity<?>> extends DomainObject<K> {

}
