package com.strawberry.app.core.context.team.aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import com.strawberry.app.core.context.team.StrawberryTeam;
import com.strawberry.app.core.context.team.command.AddStrawberryTeamCommand;
import com.strawberry.app.core.context.team.command.AmendStrawberryTeamCommand;
import com.strawberry.app.core.context.team.event.StrawberryTeamAddedEvent;
import com.strawberry.app.core.context.team.event.StrawberryTeamAmendedEvent;
import com.strawberry.app.core.context.team.identities.StrawberryTeamId;
import com.strawberry.app.core.context.team.projection.StrawberryTeamProjectionEvent;
import com.strawberry.app.core.context.team.properties.HasStrawberryTeamId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StrawberryTeamAggregate {

  @AggregateIdentifier
  @Getter
  StrawberryTeamId identity;

  @Getter
  @Setter
  StrawberryTeam team;

  public StrawberryTeamAggregate() {
  }

  @CommandHandler
  public StrawberryTeamAggregate(AddStrawberryTeamCommand command) {
    apply(StrawberryTeamAddedEvent.builder()
        .from((HasStrawberryTeamId) command)
        .build());
  }

  @EventHandler
  public void on(StrawberryTeamAddedEvent event) {
    this.identity = event.identity();
    this.team = StrawberryTeam.builder()
        .from((HasStrawberryTeamId) event)
        .build();

    apply(StrawberryTeamProjectionEvent.builder()
        .from((HasStrawberryTeamId) team)
        .createdBy(team.createdBy())
        .build());
  }

  @CommandHandler
  public void handle(AmendStrawberryTeamCommand command) {
    apply(StrawberryTeamAmendedEvent.builder()
        .from((HasStrawberryTeamId) command)
        .build());
  }

  @EventHandler
  public void on(StrawberryTeamAmendedEvent event) {
    if (event.identity().equals(identity)) {
      this.team = StrawberryTeam.builder()
          .from(team)
          .from((HasStrawberryTeamId) event)
          .modifiedAt(event.modifiedAt())
          .modifiedBy(event.modifiedBy())
          .build();

      apply(StrawberryTeamProjectionEvent.builder()
          .from((HasStrawberryTeamId) team)
          .createdBy(team.createdBy())
          .build());
    }
  }
}
