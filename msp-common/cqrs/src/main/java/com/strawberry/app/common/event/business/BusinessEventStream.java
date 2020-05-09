package com.strawberry.app.common.event.business;

import com.strawberry.app.common.Identity;
import com.strawberry.app.common.event.EventStream;

public interface BusinessEventStream<K extends Identity<?>, E extends BusinessEvent<K>> extends EventStream<K, E> {

}
