package com.strawberry.app.common.annotation;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.immutables.annotate.InjectAnnotation;
import org.immutables.annotate.InjectAnnotation.Where;

@InjectAnnotation(type = TargetAggregateIdentifier.class, target = Where.FIELD)
public @interface InjectTargetAggregateIdentifier {

}
