package com.strawberry.app.core.context.cqrscommon.projection;

import com.strawberry.app.core.context.cqrscommon.Identity;

public interface State<K extends Identity<?>> extends Projection<K> {

}
