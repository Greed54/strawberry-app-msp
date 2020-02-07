package com.strawberry.app.common.behavior;

import com.strawberry.app.common.Command;
import com.strawberry.app.common.Identity;
import com.strawberry.app.common.event.BusinessEvent;
import com.strawberry.app.common.projection.State;
import java.util.Collection;
import java.util.Optional;

public interface Behavior<Key extends Identity<?>,
    EventValue extends BusinessEvent<Key>,
    CommandValue extends Command<Key>,
    StateValue extends State<Key>> {

  Collection<EventValue> commandToEvents(CommandValue command, Optional<StateValue> state);

  StateValue eventToState(EventValue event, Optional<StateValue> current);

  Collection<Class<? extends CommandValue>> getSupportedCommands();

  Collection<Class<? extends EventValue>> getSupportedEvents();

  Class<? extends StateValue> getSupportedState();

}
