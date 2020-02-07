package com.strawberry.app.common.cqengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class StateSerdes<K, V> {

  final ObjectMapper objectMapper;
  static Logger LOGGER = LoggerFactory.getLogger(StateSerdes.class);

  public StateSerdes(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public byte[] rawKey(K key) {
    byte[] rawKey = new byte[0];
    try {
      rawKey = objectMapper.writeValueAsBytes(key);
    } catch (JsonProcessingException e) {
      LOGGER.error("Cannot be serialize key {} in bytes", key);
    }
    return rawKey;
  }

  public byte[] rawValue(V value) {
    byte[] rawValue = new byte[0];
    try {
      rawValue = objectMapper.writeValueAsBytes(value);
    } catch (JsonProcessingException e) {
      LOGGER.error("Cannot be serialize value {} in bytes", value);
    }
    return rawValue;
  }

  public K keyFrom(byte[] rawKey, Class<K> kClass) {
    K key = null;
    if (Objects.nonNull(rawKey)) {
      try {
        key = objectMapper.readValue(rawKey, kClass);
      } catch (IOException e) {
        LOGGER.error("Cannot be deserialize key class {} from bytes", kClass);
      }
    }
    return key;
  }

  public V valueFrom(byte[] rawValue, Class<V> vClass) {
    V value = null;
    if (Objects.nonNull(rawValue)) {
      try {
        value = objectMapper.readValue(rawValue, vClass);
      } catch (IOException e) {
        LOGGER.error("Cannot be deserialize value class {} from bytes", vClass);
      }
    }
    return value;
  }
}
