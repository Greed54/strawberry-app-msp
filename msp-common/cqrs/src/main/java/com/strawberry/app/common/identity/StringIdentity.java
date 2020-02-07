package com.strawberry.app.common.identity;

import com.strawberry.app.common.Identity;

public interface StringIdentity extends Identity<String> {

  @Override
  String value();
}
