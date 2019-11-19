package com.strawberry.app.core.application;

import com.strawberry.app.core.context.employee.aggregate.StrawberryEmployeeAggregate;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

//  @Bean
//  public EventSourcingRepository<StrawberryEmployeeAggregate> employeeAggregateEventSourcingRepository(EventStore eventStore) {
//    return EventSourcingRepository.builder(StrawberryEmployeeAggregate.class)
//        .eventStore(eventStore)
//        .build();
//  }

}
