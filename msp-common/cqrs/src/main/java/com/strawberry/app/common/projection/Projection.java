package com.strawberry.app.common.projection;

import com.strawberry.app.common.DomainObject;
import com.strawberry.app.common.Identity;

public interface Projection<K extends Identity<?>> extends DomainObject<K> {

}
