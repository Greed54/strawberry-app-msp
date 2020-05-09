package com.strawberry.app.common.projection;

import com.strawberry.app.common.Identity;
import com.strawberry.app.common.event.EventStream;

public interface ProjectionEventStream<K extends Identity<?>, PE extends ProjectionEvent<K>> extends EventStream<K, PE> {

}
