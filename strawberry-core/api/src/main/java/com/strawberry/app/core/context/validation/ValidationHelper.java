package com.strawberry.app.core.context.validation;

import static com.strawberry.app.core.context.validation.ValidationUtils.validateStateActive;
import static com.strawberry.app.core.context.validation.ValidationUtils.validateStateNotPresent;
import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toSet;

import com.strawberry.app.core.context.common.property.context.HasRemoved;
import com.strawberry.app.core.context.cqrscommon.event.BusinessEvent;
import com.strawberry.app.core.context.cqrscommon.Command;
import com.strawberry.app.core.context.cqrscommon.Identity;
import com.strawberry.app.core.context.cqrscommon.projection.State;
import com.strawberry.app.core.context.validation.context.ValidationResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValidationHelper<K extends Identity<?>, S extends State<K> & HasRemoved, E extends BusinessEvent<K>, C extends Command<K>> {

  final ArrayList<String> failedMessages = new ArrayList<>();

  final Optional<S> state;
  final C command;

  BiFunction<C, S, E> resultMapper;
  BiFunction<C, S, Collection<E>> resultCollectionMapper;

  public ValidationHelper(S state) {
    this.state = Optional.ofNullable(state);
    this.command = null;
  }

  public ValidationHelper<K, S, E, C> success(BiFunction<C, S, E> mapper) {
    resultMapper = mapper;
    return this;
  }

  public ValidationHelper<K, S, E, C> successCollection(BiFunction<C, S, Collection<E>> mapper) {
    resultCollectionMapper = mapper;
    return this;
  }

  public ValidationHelper<K, S, E, C> success(Predicate<C> condition, BiFunction<C, S, E> upMapper, BiFunction<C, S, E> downMapper) {
    resultMapper = condition.test(command) ? upMapper : downMapper;
    return this;
  }

  public ValidationHelper<K, S, E, C> validate(BiFunction<C, S, ValidationResult> validationResultSupplier) {
    saveResult(validationResultSupplier.apply(command, state.orElse(null)));
    return this;
  }

  public ValidationHelper<K, S, E, C> validateIf(Predicate<C> condition, BiFunction<C, S, ValidationResult> validationResultSupplier) {
    if (condition.test(command)) {
      saveResult(validationResultSupplier.apply(command, state.orElse(null)));
    }
    return this;
  }

  public ValidationHelper<K, S, E, C> validateIfPresent(BiFunction<C, S, ValidationResult> validationResultSupplier) {
    state.ifPresent(i -> saveResult(validationResultSupplier.apply(command, i)));
    return this;
  }

  public ValidationHelper<K, S, E, C> validateCollection(BiFunction<C, S, Collection<ValidationResult>> validationResultSupplier) {
    validationResultSupplier.apply(command, state.orElse(null)).forEach(this::saveResult);
    return this;
  }

  public ValidationHelper<K, S, E, C> validateCollectionIfPresent(BiFunction<C, S, Collection<ValidationResult>> validationResultSupplier) {
    state.ifPresent(i -> validationResultSupplier.apply(command, i).forEach(this::saveResult));
    return this;
  }

  public ValidationHelper<K, S, E, C> notPresent() {
    saveResult(validateStateNotPresent(state));
    return this;
  }

  public ValidationHelper<K, S, E, C> present(Class<S> stateClass) {
    saveResult(validateStateActive(state, stateClass));
    return this;
  }

  private void saveResult(ValidationResult validationResult) {
    if (!validationResult.isValid()) {
      failedMessages.add(validationResult.getMessage());
    }
  }

  private boolean isValid() {
    return failedMessages.isEmpty();
  }

  public Collection<E> failed(BiFunction<C, ArrayList<String>, E> failedEventMapper) {
    if (isValid()) {
      return Stream.concat(
          Optional.ofNullable(resultCollectionMapper).stream()
              .flatMap(mapper -> mapper.apply(command, state.orElse(null)).stream()),
          Optional.ofNullable(resultMapper).stream()
              .map(mapper -> mapper.apply(command, state.orElse(null)))
      ).collect(toSet());
    }
    return singleton(failedEventMapper.apply(command, failedMessages));
  }

  public Optional<E> failedSingle(BiFunction<C, ArrayList<String>, E> failedEventMapper) {
    return failed(failedEventMapper).stream().findFirst();
  }

}
