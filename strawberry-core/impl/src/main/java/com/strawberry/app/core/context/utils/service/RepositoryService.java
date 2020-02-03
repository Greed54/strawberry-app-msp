package com.strawberry.app.core.context.utils.service;

import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.resultset.ResultSet;
import com.googlecode.cqengine.resultset.common.NonUniqueObjectException;
import com.strawberry.app.core.context.common.cqengine.RepositoryFactory;
import com.strawberry.app.core.context.cqrscommon.Identity;
import com.strawberry.app.core.context.cqrscommon.projection.Projection;
import com.strawberry.app.core.context.cqrscommon.projection.State;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RepositoryService {

  static Logger LOGGER = LoggerFactory.getLogger(RepositoryService.class);
  RepositoryFactory repositoryFactory;

  public <K extends Identity<?>, V extends State<K>> Optional<V> retrieveState(K key, Class<V> valueClass) {
    if (key == null) {
      LOGGER.info("Retrieve state {} failed. Key is null", valueClass);
      return Optional.empty();
    }
    return Optional.ofNullable(repositoryFactory.getStateStore(valueClass).get(key));
  }

  public <K extends Identity<?>, V extends Projection<K>> Optional<V> retrieve(K key, Class<V> valueClass) {
    if (key == null) {
      LOGGER.info("Retrieve {} failed. Key is null", valueClass);
      return Optional.empty();
    }
    return Optional.ofNullable(repositoryFactory.getStateStore(valueClass).get(key));
  }

  public <K extends Identity<?>, V extends Projection<K>> List<V> retrieve(Query<V> query, Class<V> valueClass) {
    if (query == null) {
      LOGGER.info("Retrieve {} failed. Query is null", valueClass);
      return Collections.emptyList();
    }
    try (ResultSet<V> result = repositoryFactory.getStateStore(valueClass).retrieve(query)) {
      return result.stream().collect(Collectors.toList());
    }
  }

  public <K extends Identity<?>, V extends Projection<K>> Optional<V> retrieveUnique(Query<V> query, Class<V> valueClass) {
    if (query == null) {
      LOGGER.info("Retrieve view one {} failed. Query is null", valueClass);
      return Optional.empty();
    }
    try (ResultSet<V> result = repositoryFactory.getStateStore(valueClass).retrieve(query)) {
      if (result.size() > 1) {
        throw new NonUniqueObjectException("ResultSet contains more than one object");
      }
      return result.stream().findFirst();
    }
  }

  public <K extends Identity<?>, V extends Projection<K>> V saveProjection(K key, V projection) {
    repositoryFactory.getStateStore(projection.getClass()).put(key, projection);
    return projection;
  }
}
