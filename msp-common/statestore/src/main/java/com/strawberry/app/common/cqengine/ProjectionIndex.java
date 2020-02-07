package com.strawberry.app.common.cqengine;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.index.Index;
import com.googlecode.cqengine.index.compound.CompoundIndex;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.radix.RadixTreeIndex;
import com.googlecode.cqengine.index.radixinverted.InvertedRadixTreeIndex;
import com.googlecode.cqengine.index.radixreversed.ReversedRadixTreeIndex;
import com.googlecode.cqengine.index.suffix.SuffixTreeIndex;
import com.googlecode.cqengine.index.support.Factory;
import com.googlecode.cqengine.resultset.stored.StoredResultSet;

public interface ProjectionIndex<O> {

  Index<O> supply(Factory<StoredResultSet<O>> valueSetFactory);

  static <O, A> ProjectionIndex<O> hash(Attribute<O, A> attribute) {
    return valueSetFactory -> HashIndex.onAttribute(new HashIndex.DefaultIndexMapFactory<>(), valueSetFactory, attribute);
  }

  static <O, A extends Comparable<A>> ProjectionIndex<O> navigable(Attribute<O, A> attribute) {
    return valueSetFactory -> NavigableIndex.onAttribute(new NavigableIndex.DefaultIndexMapFactory<>(), valueSetFactory, attribute);
  }

  @SafeVarargs
  static <O> ProjectionIndex<O> compound(Attribute<O, ?>... attributes) {
    return valueSetFactory -> CompoundIndex.onAttributes(new CompoundIndex.DefaultIndexMapFactory<>(), valueSetFactory, attributes);
  }

  static <O, A extends CharSequence> ProjectionIndex<O> radixTree(Attribute<O, A> attribute) {
    return valueSetFactory -> new RadixTreeIndex<A, O>(attribute) {
      @Override
      public StoredResultSet<O> createValueSet() {
        return valueSetFactory.create();
      }
    };
  }

  static <O, A extends CharSequence> ProjectionIndex<O> reversedRadixTree(Attribute<O, A> attribute) {
    return valueSetFactory -> new ReversedRadixTreeIndex<A, O>(attribute) {
      @Override
      public StoredResultSet<O> createValueSet() {
        return valueSetFactory.create();
      }
    };
  }

  static <O, A extends CharSequence> ProjectionIndex<O> invertedRadixTree(Attribute<O, A> attribute) {
    return valueSetFactory -> new InvertedRadixTreeIndex<A, O>(attribute) {
      @Override
      public StoredResultSet<O> createValueSet() {
        return valueSetFactory.create();
      }
    };
  }

  static <O, A extends CharSequence> ProjectionIndex<O> suffixTree(Attribute<O, A> attribute) {
    return valueSetFactory -> new SuffixTreeIndex<A, O>(attribute) {
      @Override
      public StoredResultSet<O> createValueSet() {
        return valueSetFactory.create();
      }
    };
  }
}
