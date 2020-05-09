package com.strawberry.app.core.context.person.service;

import com.strawberry.app.core.context.person.StrawberryPerson;
import com.strawberry.app.core.context.person.identities.StrawberryPersonId;
import com.strawberry.app.core.context.utils.service.RepositoryService;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class StrawberryPersonService {

  RepositoryService repositoryService;

  public Optional<StrawberryPerson> getPerson(StrawberryPersonId personId) {
    return repositoryService.retrieveState(personId, StrawberryPerson.class);
  }
}
