package com.strawberry.app.core.context.cqrscommon.identity;

import com.strawberry.app.core.context.cqrscommon.Identity;

public interface StringIdentity extends Identity<String> {

  @Override
  String value();
}
