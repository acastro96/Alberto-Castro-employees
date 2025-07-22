package com.alberto.sirmatest.configuration;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
public class ExecutorShutdown {

  private final ExecutorService processingExecutor;

  public ExecutorShutdown(@Qualifier("processingExecutor") ExecutorService processingExecutor) {
    this.processingExecutor = processingExecutor;
  }

  @PreDestroy
  public void shutdownExecutor() {
    processingExecutor.shutdown();
  }

}
