package com.strawberry.app.core.context.box.behavior;

import com.strawberry.app.common.ValidationHelper;
import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.property.context.identity.card.CardId;
import com.strawberry.app.core.context.box.StrawberryBox;
import com.strawberry.app.core.context.box.command.AddStrawberryBoxCommand;
import com.strawberry.app.core.context.box.command.StrawberryBoxCommand;
import com.strawberry.app.core.context.box.event.StrawberryBoxAddedEvent;
import com.strawberry.app.core.context.box.event.StrawberryBoxEvent;
import com.strawberry.app.core.context.box.identities.StrawberryBoxId;
import com.strawberry.app.core.context.box.properties.HasStrawberryBoxId;
import com.strawberry.app.core.context.box.utils.StrawberryBoxEventBuilderUtils;
import com.strawberry.app.core.context.box.utils.StrawberryBoxValidator;
import com.strawberry.app.core.context.employee.service.StrawberryEmployeeService;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class AddStrawberryBoxBehavior implements Behavior<StrawberryBoxId, StrawberryBoxEvent, StrawberryBoxCommand, StrawberryBox> {

  StrawberryBoxValidator validator;
  StrawberryEmployeeService strawberryEmployeeService;

  @Override
  public Collection<StrawberryBoxEvent> commandToEvents(StrawberryBoxCommand command, Optional<StrawberryBox> state) {
    return new ValidationHelper<StrawberryBoxId, StrawberryBox, StrawberryBoxEvent, AddStrawberryBoxCommand>(state, (AddStrawberryBoxCommand) command)
        .notPresent()
        .validate((addStrawberryBoxCommand, strawberryBox) -> validator.validateCardId(new CardId(addStrawberryBoxCommand.cardId())))
        .success((addStrawberryBoxCommand, strawberryBox) ->
            StrawberryBoxAddedEvent.builder()
                .from((HasStrawberryBoxId) addStrawberryBoxCommand)
                .employeeId(strawberryEmployeeService.getEmployeesByCardId(new CardId(addStrawberryBoxCommand.cardId())).stream().findFirst().get()
                    .identity())
                .build())
        .failed(StrawberryBoxEventBuilderUtils::buildFailedEvent);
  }

  @Override
  public StrawberryBox eventToState(StrawberryBoxEvent event, Optional<StrawberryBox> current) {
    return StrawberryBox.builder()
        .from(event)
        .build();
  }

  @Override
  public Collection<Class<? extends StrawberryBoxCommand>> getSupportedCommands() {
    return Collections.singletonList(AddStrawberryBoxCommand.class);
  }

  @Override
  public Collection<Class<? extends StrawberryBoxEvent>> getSupportedEvents() {
    return Collections.singletonList(StrawberryBoxAddedEvent.class);
  }

  @Override
  public Class<? extends StrawberryBox> getSupportedState() {
    return StrawberryBox.class;
  }
}
