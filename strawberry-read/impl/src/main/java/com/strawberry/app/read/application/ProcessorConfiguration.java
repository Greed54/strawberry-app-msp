package com.strawberry.app.read.application;

import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessorConfiguration {

  @Autowired
  public void config(EventProcessingConfigurer configurer) {
    configurer.registerTrackingEventProcessorConfiguration(
        configuration -> TrackingEventProcessorConfiguration.forParallelProcessing(2)
    );
  }
}
