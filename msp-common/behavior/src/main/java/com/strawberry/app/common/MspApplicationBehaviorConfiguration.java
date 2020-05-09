package com.strawberry.app.common;

import com.strawberry.app.common.behavior.Behavior;
import com.strawberry.app.common.behavior.DefaultBehaviorEngine;
import com.strawberry.app.common.cqengine.RepositoryFactory;
import com.strawberry.app.common.viewprojector.external.ExternalAbstractProjectionAdapter;
import com.strawberry.app.common.viewprojector.external.ExternalProjectionAdapterEngine;
import com.strawberry.app.common.viewprojector.internal.InternalAbstractProjectionAdapter;
import com.strawberry.app.common.viewprojector.internal.InternalProjectionAdapterEngine;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MspApplicationBehaviorConfiguration {

  @Bean
  public InternalProjectionAdapterEngine internalProjectionAdapterEngine(List<InternalAbstractProjectionAdapter> internalAbstractProjectionAdapters) {
    return new InternalProjectionAdapterEngine(internalAbstractProjectionAdapters);
  }

  @Bean
  public ExternalProjectionAdapterEngine externalProjectionAdapterEngine(List<ExternalAbstractProjectionAdapter> externalAbstractProjectionAdapters) {
    return new ExternalProjectionAdapterEngine(externalAbstractProjectionAdapters);
  }

  @Bean
  public DefaultBehaviorEngine defaultBehaviorEngine(RepositoryFactory repositoryFactory, List<Behavior> behaviors) {
    return new DefaultBehaviorEngine(repositoryFactory, behaviors);
  }
}
