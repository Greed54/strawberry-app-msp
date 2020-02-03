package com.strawberry.app.core.context.common.rocksdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import org.rocksdb.BlockBasedTableConfig;
import org.rocksdb.CompactionStyle;
import org.rocksdb.CompressionType;
import org.rocksdb.FlushOptions;
import org.rocksdb.InfoLogLevel;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import org.rocksdb.WriteBatch;
import org.rocksdb.WriteOptions;

public class RocksDBStore implements KeyValueStore<Bytes, byte[]> {

  private static final CompressionType COMPRESSION_TYPE;
  private static final CompactionStyle COMPACTION_STYLE;
  File dbDir;
  private RocksDB db;
  private Options options;
  private WriteOptions wOptions;
  private FlushOptions fOptions;
  private final String parentDir;
  protected final String name;
  private final Set<KeyValueIterator> openIterators;
  protected volatile boolean open;

  public RocksDBStore(String name) {
    this(name, "rocksDb");
  }

  public RocksDBStore(String name, String parentDir) {
    this.openIterators = Collections.synchronizedSet(new HashSet());
    this.name = name;
    this.parentDir = parentDir;
  }

  public void openDB(String stateDir) {
    BlockBasedTableConfig tableConfig = new BlockBasedTableConfig();
    tableConfig.setBlockSize(4096L);
    this.options = new Options();
    this.options.setTableFormatConfig(tableConfig);
    this.options.setWriteBufferSize(16777216L);
    this.options.setCompressionType(COMPRESSION_TYPE);
    this.options.setCompactionStyle(COMPACTION_STYLE);
    this.options.setMaxWriteBufferNumber(3);
    this.options.setCreateIfMissing(true);
    this.options.setErrorIfExists(false);
    this.options.setInfoLogLevel(InfoLogLevel.DEBUG_LEVEL);
    this.options.setIncreaseParallelism(Math.max(Runtime.getRuntime().availableProcessors(), 2));

    this.wOptions = new WriteOptions();
    this.wOptions.setDisableWAL(true);
    this.fOptions = new FlushOptions();
    this.fOptions.setWaitForFlush(true);

    this.dbDir = new File(new File(stateDir, this.parentDir), this.name);

    try {
      this.db = this.openDB(this.dbDir, this.options, -1);
    } catch (IOException var6) {
      var6.getStackTrace();
    }

    this.open = true;
  }

  private RocksDB openDB(File dir, Options options, int ttl) throws IOException {
    try {
      if (ttl == -1) {
        Files.createDirectories(dir.getParentFile().toPath());
        return RocksDB.open(options, dir.getAbsolutePath());
      } else {
        throw new UnsupportedOperationException("Change log is not supported for store " + this.name + " since it is TTL based.");
      }
    } catch (RocksDBException var5) {
      throw new UnsupportedOperationException("Error opening store " + this.name + " at location " + dir.toString(), var5);
    }
  }

  @Override
  public synchronized void put(Bytes key, byte[] value) {
    Objects.requireNonNull(key, "key cannot be null");
    this.validateStoreOpen();
    this.putInternal(key.get(), value);
  }

  private void putInternal(byte[] rawKey, byte[] rawValue) {
    if (rawValue == null) {
      try {
        this.db.delete(this.wOptions, rawKey);
      } catch (RocksDBException var5) {
        throw new UnsupportedOperationException("Error while removing key from store " + this.name, var5);
      }
    } else {
      try {
        this.db.put(this.wOptions, rawKey, rawValue);
      } catch (RocksDBException var4) {
        throw new UnsupportedOperationException("Error while executing putting key/value into store " + this.name, var4);
      }
    }

  }

  @Override
  public synchronized byte[] putIfAbsent(Bytes key, byte[] value) {
    Objects.requireNonNull(key, "key cannot be null");
    byte[] originalValue = this.get(key);
    if (originalValue == null) {
      this.put(key, value);
    }

    return originalValue;
  }

  @Override
  public synchronized void putAll(List<KeyValue<Bytes, byte[]>> entries) {
    try {
      WriteBatch batch = new WriteBatch();

      try {
        Iterator var3 = entries.iterator();

        while (var3.hasNext()) {
          KeyValue<Bytes, byte[]> entry = (KeyValue) var3.next();
          Objects.requireNonNull(entry.key, "key cannot be null");
          if (entry.value == null) {
            batch.delete(entry.key.get());
          } else {
            batch.put(entry.key.get(), entry.value);
          }
        }

        this.db.write(this.wOptions, batch);
      } catch (Throwable var6) {
        try {
          batch.close();
        } catch (Throwable var5) {
          var6.addSuppressed(var5);
        }

        throw var6;
      }

      batch.close();
    } catch (RocksDBException var7) {
      throw new UnsupportedOperationException("Error while batch writing to store " + this.name, var7);
    }
  }

  @Override
  public synchronized byte[] delete(Bytes key) {
    Objects.requireNonNull(key, "key cannot be null");
    byte[] value = this.get(key);
    this.put(key, null);
    return value;
  }

  @Override
  public synchronized byte[] get(Bytes key) {
    this.validateStoreOpen();
    return this.getInternal(key.get());
  }

  @Override
  public KeyValueIterator<Bytes, byte[]> range(Bytes from, Bytes to) {
    Objects.requireNonNull(from, "from cannot be null");
    Objects.requireNonNull(to, "to cannot be null");
    this.validateStoreOpen();
    RocksDBStore.RocksDBRangeIterator rocksDBRangeIterator = new RocksDBStore.RocksDBRangeIterator(this.name, this.rawIterator(), from, to);
    this.openIterators.add(rocksDBRangeIterator);
    return rocksDBRangeIterator;
  }

  RocksIterator rawIterator() {
    return this.db.newIterator();
  }

  void validateStoreOpen() {
    if (!this.open) {
      throw new UnsupportedOperationException("Store " + this.name + " is currently closed");
    }
  }

  private byte[] getInternal(byte[] rawKey) {
    try {
      return this.db.get(rawKey);
    } catch (RocksDBException var3) {
      throw new UnsupportedOperationException("Error while getting value for key from store " + this.name, var3);
    }
  }


  @Override
  public KeyValueIterator<Bytes, byte[]> all() {
    this.validateStoreOpen();
    RocksIterator innerIter = this.rawIterator();
    innerIter.seekToFirst();
    RocksDBStore.RocksDbIterator rocksDbIterator = new RocksDBStore.RocksDbIterator(this.name, innerIter);
    this.openIterators.add(rocksDbIterator);
    return rocksDbIterator;
  }

  @Override
  public long approximateNumEntries() {
    this.validateStoreOpen();

    long value;
    try {
      value = this.db.getLongProperty("rocksdb.estimate-num-keys");
    } catch (RocksDBException var4) {
      throw new UnsupportedOperationException("Error fetching property from store " + this.name, var4);
    }

    return this.isOverflowing(value) ? 9223372036854775807L : value;
  }

  @Override
  public String name() {
    return this.name;
  }

  @Override
  public void init(String stateDir, ObjectMapper objectMapper) {
    this.openDB(stateDir);
  }

  @Override
  public synchronized void flush() {
    if (this.db != null) {
      this.flushInternal();
    }
  }

  private void flushInternal() {
    try {
      this.db.flush(this.fOptions);
    } catch (RocksDBException var2) {
      throw new UnsupportedOperationException("Error while executing flush from store " + this.name, var2);
    }
  }

  public synchronized void close() {
    if (this.open) {
      this.open = false;
      this.closeOpenIterators();
      this.options.close();
      this.wOptions.close();
      this.fOptions.close();
      this.db.close();
      this.options = null;
      this.wOptions = null;
      this.fOptions = null;
      this.db = null;
    }
  }

  @Override
  public boolean persistent() {
    return true;
  }

  @Override
  public boolean isOpen() {
    return this.open;
  }

  private void closeOpenIterators() {
    HashSet iterators;
    synchronized (this.openIterators) {
      iterators = new HashSet(this.openIterators);
    }

    Iterator var2 = iterators.iterator();

    while (var2.hasNext()) {
      KeyValueIterator iterator = (KeyValueIterator) var2.next();
      iterator.close();
    }

  }

  private boolean isOverflowing(long value) {
    return value < 0L;
  }


  static {
    COMPRESSION_TYPE = CompressionType.NO_COMPRESSION;
    COMPACTION_STYLE = CompactionStyle.UNIVERSAL;
  }

  private class RocksDBRangeIterator extends RocksDBStore.RocksDbIterator {

    private final Comparator<byte[]> comparator;
    private final byte[] rawToKey;

    RocksDBRangeIterator(String storeName, RocksIterator iter, Bytes from, Bytes to) {
      super(storeName, iter);
      this.comparator = Bytes.BYTES_LEXICO_COMPARATOR;
      iter.seek(from.get());
      this.rawToKey = to.get();
      if (this.rawToKey == null) {
        throw new NullPointerException("RocksDBRangeIterator: RawToKey is null for key " + to);
      }
    }

    public synchronized boolean hasNext() {
      return super.hasNext() && this.comparator.compare(super.peekRawKey(), this.rawToKey) <= 0;
    }
  }

  class RocksDbIterator implements KeyValueIterator<Bytes, byte[]> {

    private final String storeName;
    private final RocksIterator iter;
    private volatile boolean open = true;

    RocksDbIterator(String storeName, RocksIterator iter) {
      this.iter = iter;
      this.storeName = storeName;
    }

    byte[] peekRawKey() {
      return this.iter.key();
    }

    private KeyValue<Bytes, byte[]> getKeyValue() {
      return new KeyValue(new Bytes(this.iter.key()), this.iter.value());
    }

    public synchronized boolean hasNext() {
      if (!this.open) {
        throw new UnsupportedOperationException(String.format("RocksDB store %s has closed", this.storeName));
      } else {
        return this.iter.isValid();
      }
    }

    public synchronized KeyValue<Bytes, byte[]> next() {
      if (!this.hasNext()) {
        throw new NoSuchElementException();
      } else {
        KeyValue<Bytes, byte[]> entry = this.getKeyValue();
        this.iter.next();
        return entry;
      }
    }

    public void remove() {
      throw new UnsupportedOperationException("RocksDB iterator does not support remove()");
    }

    public synchronized void close() {
      RocksDBStore.this.openIterators.remove(this);
      this.iter.close();
      this.open = false;
    }

    public Bytes peekNextKey() {
      if (!this.hasNext()) {
        throw new NoSuchElementException();
      } else {
        return new Bytes(this.iter.key());
      }
    }
  }
}
