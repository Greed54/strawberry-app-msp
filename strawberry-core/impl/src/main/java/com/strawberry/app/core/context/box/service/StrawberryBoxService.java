package com.strawberry.app.core.context.box.service;

import com.strawberry.app.core.context.box.StrawberryBox;
import com.strawberry.app.core.context.box.identities.StrawberryBoxId;
import com.strawberry.app.core.context.utils.service.RepositoryService;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class StrawberryBoxService {

  RepositoryService repositoryService;

  public Optional<StrawberryBox> getBox(StrawberryBoxId boxId) {
    return repositoryService.retrieveState(boxId, StrawberryBox.class);
  }
}
