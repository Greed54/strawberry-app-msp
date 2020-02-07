package com.strawberry.app.common;

import static com.strawberry.app.common.context.ValidationFailureType.EXIST;
import static com.strawberry.app.common.context.ValidationFailureType.REMOVED;
import static java.util.Collections.emptySet;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;

import com.google.common.collect.Sets;
import com.strawberry.app.common.context.ValidationFailure;
import com.strawberry.app.common.context.ValidationFailureType;
import com.strawberry.app.common.context.ValidationResult;
import com.strawberry.app.common.context.ValidationSuccess;
import com.strawberry.app.common.projection.State;
import com.strawberry.app.common.property.context.HasRemoved;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public class ValidationUtils {

  private static final Pattern UUID_PATTERN = Pattern.compile(".*[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}");

  /**
   * Validate String name for null, blank, whitespace.
   *
   * @param name String to validate, may be null or empty
   * @return the ValidationResult, ValidationSuccess if String is not blank or null or whitespace only, otherwise ValidationFailure
   */
  public static ValidationResult validateName(String name) {
    if (isNoneBlank(name)) {
      return generateValidationSuccess();
    }
    return generateValidationFailure("Name", name);
  }

  /**
   * Validate Collection is not null and not empty
   *
   * @param collection     Collection to validate, may be null or empty
   * @param collectionName collection name
   * @return the ValidationResult, ValidationSuccess if Collection is not null and not empty, otherwise ValidationFailure
   */
  public static <T> ValidationResult validateCollectionNotEmpty(Collection<T> collection, String collectionName) {
    if (isNotEmpty((collection))) {
      return generateValidationSuccess();
    }
    return generateValidationFailure(collectionName, "is null or empty");
  }

  /**
   * Validate Collection is null or empty
   *
   * @param collection     Collection to validate, may be null or empty
   * @param collectionName collection name
   * @return the ValidationResult, ValidationSuccess if Collection is null or empty, otherwise ValidationFailure
   */
  public static <T> ValidationResult validateCollectionEmpty(Collection<T> collection, String collectionName) {
    if (isEmpty((collection))) {
      return generateValidationSuccess();
    }
    return generateValidationFailure(collectionName, "is not empty");
  }

  /**
   * Validate two values are equal
   *
   * @param sourceValue     value to compare
   * @param comparableValue value with which compare
   * @return the ValidationResult, ValidationSuccess if values are equal, otherwise ValidationFailure
   */
  public static <T> ValidationResult validateEquals(T sourceValue, T comparableValue) {
    if (Objects.equals(sourceValue, comparableValue)) {
      return generateValidationSuccess();
    }
    return generateValidationFailure("%s is not equal to %s", sourceValue, comparableValue);
  }

  public static <T> ValidationResult validateEquals(Set<T> sourceValue, Set<T> comparableValue) {
    return Optional.of(Sets.difference(sourceValue, comparableValue))
        .filter(diff -> !diff.isEmpty())
        .<ValidationResult>map(
            diff -> generateValidationFailure("The collections have differences: %s", diff.stream().map(Objects::toString).collect(joining(","))))
        .orElseGet(ValidationUtils::generateValidationSuccess);
  }

  /**
   * Validate two values are not equal
   *
   * @param sourceValue     value to compare
   * @param comparableValue value with which compare
   * @return the ValidationResult, ValidationSuccess if values are not equal, otherwise ValidationFailure
   */
  public static <T> ValidationResult validateNotEquals(T sourceValue, T comparableValue) {
    if (Objects.equals(sourceValue, comparableValue)) {
      return generateValidationFailure("%s is equal to %s", sourceValue, comparableValue);
    }
    return generateValidationSuccess();
  }

  /**
   * Compares two instants like date interval.
   *
   * @param dateFrom       start interval, not null
   * @param dateTo         end interval, not null
   * @param failureMessage message in validation failure case
   * @return the ValidationResult, ValidationSuccess if end interval is greater or equals than start interval, otherwise ValidationFailure
   */
  public static ValidationResult validateDateInterval(Instant dateFrom, Instant dateTo, String failureMessage) {
    if (dateFrom != null && dateTo != null && dateFrom.truncatedTo(ChronoUnit.DAYS).compareTo(dateTo.truncatedTo(ChronoUnit.DAYS)) <= 0) {
      return generateValidationSuccess();
    }
    return generateValidationFailure("%s = %s %s", failureMessage, dateFrom, dateTo);
  }

  /**
   * Validate State is not present
   *
   * @param state State to validate
   * @return the ValidationResult, ValidationSuccess if State is not present, otherwise ValidationFailure
   */
  public static <S extends State<K>, K extends Identity<?>> ValidationResult validateStateNotPresent(Optional<S> state) {
    if (state.isPresent()) {
      return generateValidationFailure(EXIST, state.get().identity(), state.get().getClass());
    }
    return generateValidationSuccess();
  }

  /**
   * Validate State is present and has not been removed
   *
   * @param state      State to validate
   * @param stateClass Class of state
   * @return the ValidationResult, ValidationSuccess if State is present and has not been removed, otherwise ValidationFailure
   */
  public static <S extends State<K> & HasRemoved, K extends Identity<?>> ValidationResult validateStateActive(Optional<S> state,
      Class<? extends S> stateClass) {
    if (!state.isPresent()) {
      return buildNotExistsValidationFailure(stateClass);
    }
    if (state.get().removed()) {
      return generateValidationFailure(REMOVED, state.get().identity(), state.get().getClass());
    }
    return generateValidationSuccess();
  }

  /**
   * Validate State is present and has been removed
   *
   * @param state      State to validate
   * @param stateClass Class of state
   * @return the ValidationResult, ValidationSuccess if State is present and has not been removed, otherwise ValidationFailure
   */
  public static <S extends State<K> & HasRemoved, K extends Identity<?>> ValidationResult validateStateRemoved(Optional<S> state,
      Class<? extends S> stateClass) {
    if (!state.isPresent()) {
      return generateValidationFailure(String.format("%s does not exist", cleanClass(stateClass)));
    }
    if (!state.get().removed()) {
      return generateValidationFailure(EXIST, state.get().identity(), state.get().getClass());
    }
    return generateValidationSuccess();
  }

  public static <S extends State<K>, K extends Identity<?>> ValidationFailure buildNotExistsValidationFailure(Class<S> stateClass) {
    return generateValidationFailure(String.format("%s does not exist", cleanClass(stateClass)));
  }

  /**
   * Validate State is present and has not been removed
   *
   * @param state State to validate
   * @return the ValidationResult, ValidationSuccess if State is present and has not been removed, otherwise ValidationFailure
   */
  public static <S extends State<K> & HasRemoved, K extends Identity<?>> ValidationResult validateStateNotRemoved(S state) {
    if (state.removed()) {
      return generateValidationFailure(REMOVED, state.identity(), state.getClass());
    }
    return generateValidationSuccess();
  }

  /**
   * Apply supplier with arguments (identity, valueClass) and check if result of supplier is present
   *
   * @param supplier   Supplier to apply
   * @param identity   first supplier argument
   * @param valueClass second supplier argument
   * @return the ValidationResult, ValidationSuccess if State is present and has not been removed, otherwise ValidationFailure
   */
  public static <T extends Identity<?>, U, R> ValidationResult isPresent(BiFunction<T, U, Optional<R>> supplier, T identity, U valueClass) {
    return supplier.apply(identity, valueClass)
        .map(value -> (ValidationResult) generateValidationSuccess())
        .orElseGet(() -> generateValidationFailure(identity.getClass().getSimpleName(), identity.value()));
  }

  public static <K extends Identity<?>, R> ValidationResult isPresent(Function<K, Optional<R>> supplier, K identity) {
    return supplier.apply(identity)
        .map(value -> (ValidationResult) generateValidationSuccess())
        .orElseGet(() -> generateValidationFailure(identity.getClass().getSimpleName(), identity.value()));
  }

  /**
   * Cleanup class name
   *
   * @param valueClass Class to cleanup, not null
   * @return String, cleared class name, otherwise throw IllegalArgumentException
   */
  protected static <T> String cleanClass(Class<? extends T> valueClass) {
    //TODO change to Immutable cleanup
    if (valueClass == null) {
      throw new IllegalArgumentException("Class must be not null");
    }
    return valueClass.getSimpleName();
  }

  /**
   * Generate new ValidationSuccess
   *
   * @return the new ValidationSuccess
   */
  public static ValidationSuccess generateValidationSuccess() {
    return new ValidationSuccess();
  }

  /**
   * Generate new ValidationFailure with predefined pattern
   *
   * @param valueClass Class of value
   * @param message    String message
   * @return the new ValidationFailure
   */
  public static <T> ValidationFailure generateValidationFailure(Class<? extends T> valueClass, String message) {
    return generateValidationFailure(cleanClass(valueClass), message);
  }

  /**
   * Generate new ValidationFailure with predefined pattern %s = %s
   *
   * @param prefix String value for first entry
   * @param body   String value for second entry
   * @return the new ValidationFailure
   */
  public static ValidationFailure generateValidationFailure(String prefix, String body) {
    return generateValidationFailure("%s = %s", prefix, body);
  }

  public static <T extends Identity<?>, V> ValidationFailure generateValidationFailure(ValidationFailureType validationFailureType,
      T identity, Class<? extends V> valueClass) {
    switch (validationFailureType) {
      case DUPLICATED:
        return generateValidationFailure("%s is duplicated. %s = %s", cleanClass(valueClass), cleanClass(identity.getClass()),
            identity.value());
      case EXIST:
        return generateValidationFailure("%s is exist. %s = %s", cleanClass(valueClass), cleanClass(identity.getClass()), identity.value());
      case REMOVED:
        return generateValidationFailure("%s has been removed. %s = %s", cleanClass(valueClass), cleanClass(identity.getClass()),
            identity.value());
      case ACTIVE:
        return generateValidationFailure("%s is active. %s = %s", cleanClass(valueClass), cleanClass(identity.getClass()),
            identity.value());
      case INVALID:
        return generateValidationFailure("%s is invalid. %s = %s", cleanClass(valueClass), cleanClass(identity.getClass()),
            identity.value());
      default:
        return null;
    }
  }

  /**
   * Generate new ValidationFailure with custom pattern
   *
   * @param pattern String pattern
   * @param objects array of String values for entries
   * @return the new ValidationFailure
   */
  public static ValidationFailure generateValidationFailure(String pattern, Object... objects) {
    return new ValidationFailure(String.format(pattern, objects));
  }

  public static <T> void ifPresent(T value, Consumer<? super T> action) {
    if (nonNull(value)) {
      action.accept(value);
    }
  }

  public static <T> boolean ifSwitched(T from, T to, T compare) {
    return (!from.equals(compare) && to.equals(compare)) || (from.equals(compare) && !to.equals(compare));
  }

  public static <T extends HasRemoved> Boolean isRestoreRequired(Optional<T> state) {
    return state.map(T::removed).orElse(false);
  }

  public static Boolean resolveOptionalPositive(Supplier<Boolean> optionalSupplier) {
    return Optional.ofNullable(optionalSupplier.get()).orElse(Boolean.TRUE);
  }

  public static Boolean resolveOptionalNegative(Supplier<Boolean> optionalSupplier) {
    return !resolveOptionalPositive(optionalSupplier);
  }

  public static <T> HashSet<T> immutablePopulate(Set<T> collection, T element) {
    return immutablePopulate(Optional.ofNullable(collection).orElse(emptySet()).stream(), element)
        .collect(toCollection(HashSet::new));
  }

  public static <T> Stream<T> immutablePopulate(Stream<T> collectionStream, T element) {
    return Stream.concat(
        Optional.ofNullable(collectionStream).orElse(Stream.empty()),
        Stream.of(requireNonNull(element))
    );
  }

  public static <T, I extends Identity<?>> HashSet<T> immutableFilter(Set<T> collection, T element, Function<T, I> keyExtractor) {
    return emptyIfNull(collection).stream()
        .filter(i -> !keyExtractor.apply(i).equals(keyExtractor.apply(element)))
        .collect(toCollection(HashSet::new));
  }

  public static <I extends Identity<?>, V> ValidationResult validateNotExist(Set<V> collection, V element, Function<V, I> keyExtractor) {
    return collection.stream()
        .filter(item -> keyExtractor.apply(item).equals(keyExtractor.apply(element)))
        .findFirst().<ValidationResult>map(
            existingElement -> generateValidationFailure(ValidationFailureType.EXIST, keyExtractor.apply(existingElement),
                keyExtractor.apply(existingElement).getClass()))
        .orElse(generateValidationSuccess());
  }

  public static <V> ValidationResult validateContains(Collection<V> collection, V element) {
    return validateContains(collection, element, Function.identity());
  }

  public static <V, K> ValidationResult validateContains(Collection<V> collection, V element, Function<V, K> keyExtractor) {
    return requireNonNull(collection).stream()
        .filter(i -> keyExtractor.apply(i).equals(keyExtractor.apply(i)))
        .findAny().<ValidationResult>map(i -> generateValidationSuccess())
        .orElseGet(() -> generateValidationFailure("%s does not contains in %s", element, StringUtils.join(collection, ", ")));
  }

  public static boolean isValidUUID(String uuid) {
    return isNoneBlank(uuid) && UUID_PATTERN.matcher(uuid).matches();
  }

}
