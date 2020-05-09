package com.strawberry.app.core.context.person.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import com.strawberry.app.common.aggregate.AbstractAggregate;
import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.property.context.identity.PersonId;
import com.strawberry.app.core.context.person.StrawberryPerson;
import com.strawberry.app.core.context.person.command.AddStrawberryPersonCommand;
import com.strawberry.app.core.context.person.command.StrawberryPersonCommand;
import com.strawberry.app.core.context.person.event.StrawberryPersonEvent;
import com.strawberry.app.core.context.person.event.StrawberryPersonFailedEvent;
import com.strawberry.app.core.context.person.identities.StrawberryPersonId;
import com.strawberry.app.core.context.person.projection.StrawberryPersonProjectionEvent;
import com.strawberry.app.core.context.person.properties.HasStrawberryPersonId;
import com.strawberry.app.core.context.person.service.StrawberryPersonService;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@FieldDefaults(level = AccessLevel.PRIVATE)
@ProcessingGroup("StrawberryPersonAggregate")
public class StrawberryPersonAggregate implements AbstractAggregate<StrawberryPersonId, StrawberryPersonCommand, StrawberryPersonEvent, StrawberryPerson> {

  @AggregateIdentifier
  @Getter
  StrawberryPersonId identity;
  @Getter
  StrawberryPerson person;

  public StrawberryPersonAggregate() {
  }

  @CommandHandler
  public StrawberryPersonAggregate(AddStrawberryPersonCommand command, DefaultBehaviorEngine defaultBehaviorEngine,
      StrawberryPersonService personService) {
    Behavior<StrawberryPersonId, StrawberryPersonEvent, StrawberryPersonCommand, StrawberryPerson> behavior = defaultBehaviorEngine
        .getBehavior(command.getClass());
    behavior.commandToEvents(command, personService.getPerson(command.identity())).forEach(AggregateLifecycle::apply);
  }

  @Override
  @EventHandler
  public void handleEvent(StrawberryPersonEvent businessEvent, DefaultBehaviorEngine behaviorEngine) {
    if (!(businessEvent instanceof StrawberryPersonFailedEvent)) {
      Behavior<StrawberryPersonId, StrawberryPersonEvent, StrawberryPersonCommand, StrawberryPerson> behavior = behaviorEngine
          .getBehavior(businessEvent.getClass());
      this.identity = businessEvent.identity();
      this.person = behavior.eventToState(businessEvent, Optional.ofNullable(person));

      projectState(person, behaviorEngine);
      publishProjectionEvent(person);
    } else {
      this.identity = businessEvent.identity();
    }
  }

  @Override
  public Class<StrawberryPersonId> identityClass() {
    return StrawberryPersonId.class;
  }

  @Override
  public Class<StrawberryPerson> stateClass() {
    return StrawberryPerson.class;
  }

  @Override
  public void publishProjectionEvent(StrawberryPerson state) {
    apply(StrawberryPersonProjectionEvent.builder()
        .from((HasStrawberryPersonId) state)
        .build());
  }
}
