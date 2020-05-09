package com.strawberry.app.common.event;

import com.strawberry.app.common.DomainStream;
import com.strawberry.app.common.Identity;

public interface EventStream<K extends Identity<?>, E extends Event<K>> extends DomainStream<K, E> {

}
