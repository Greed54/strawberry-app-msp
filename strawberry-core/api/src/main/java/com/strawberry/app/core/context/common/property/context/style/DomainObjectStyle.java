package com.strawberry.app.core.context.common.property.context.style;

import org.immutables.value.Value.Style;

@Style(
    typeAbstract = "I*",
    typeImmutable = "*",
    jdkOnly = true,
    privateNoargConstructor = true
)
public @interface DomainObjectStyle {

}
