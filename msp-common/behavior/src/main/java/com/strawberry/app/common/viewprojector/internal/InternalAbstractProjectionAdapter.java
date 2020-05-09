package com.strawberry.app.common.viewprojector.internal;

import com.strawberry.app.common.Identity;
import com.strawberry.app.common.event.business.BusinessEvent;
import com.strawberry.app.common.event.business.BusinessEventStream;

public interface InternalAbstractProjectionAdapter<I extends Identity<?>, BE extends BusinessEvent<I>> {

  void convert(BE businessEvent);

  BusinessEventStream<I, BE> eventStream();
}
