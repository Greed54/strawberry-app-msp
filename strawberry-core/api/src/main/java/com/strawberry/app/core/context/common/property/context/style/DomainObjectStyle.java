package com.strawberry.app.core.context.common.property.context.style;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value.Style;

@JsonSerialize
@Style(
    typeAbstract = "I*",
    typeImmutable = "*",
    jdkOnly = true,
    privateNoargConstructor = true,
    additionalJsonAnnotations = JsonSerialize.class
)
public @interface DomainObjectStyle {

}
