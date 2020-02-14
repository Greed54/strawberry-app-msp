package com.strawberry.app.common.projection;

import com.strawberry.app.common.Identity;

public interface ProjectionEventStream<K extends Identity<?>, PE extends ProjectionEvent<K>> {

  Class<K> getKeyClass();

  Class<PE> getValueClass();
}
