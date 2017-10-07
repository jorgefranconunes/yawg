/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;

import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.control.Option;

import com.varmateo.yawg.util.GlobMatcher;


/**
 *
 */
/* package private */ final class BakerMatcher {


    private Seq<Entry> _bakerTypes;


    /**
     *
     */
    private BakerMatcher(final Builder builder) {

        _bakerTypes = builder._bakerTypes
                .map(x -> new Entry(x._1, x._2));
    }


    /**
     * Creates a new builder with no initializations.
     *
     * @return A newly created <code>Builder</code> instance.
     */
    public static Builder builder() {

        return new Builder();
    }


    /**
     * Creates a new builder initialized with the data from the given
     * <code>BakerMatcher</code>.
     *
     * @param data Used for initializing the builder state.
     *
     * @return A newly created <code>Builder</code> instance.
     */
    public static Builder builder(final BakerMatcher data) {

        return new Builder(data);
    }


    /**
     *
     */
    public Option<String> getBakerTypeFor(final Path path) {

        return _bakerTypes
                .filter(entry -> entry.matcher.test(path))
                .map(entry -> entry.mapType)
                .headOption();
    }


    /**
     * Intended for logging or debugging.
     *
     * @return The string representation of our matchers.
     */
    @Override
    public String toString() {

        return _bakerTypes
                .sorted((o1, o2) -> o1.mapType.compareTo(o2.mapType))
                .map(e -> e.mapType + ":" + e.matcher.toString())
                .mkString(";");
    }


    /**
     *
     */
    private static final class Entry {


        public final String mapType;
        public final GlobMatcher matcher;


        /**
         *
         */
        Entry(
                final String mapType,
                final GlobMatcher matcher) {

            this.mapType = mapType;
            this.matcher = matcher;
        }


    }


    /**
     *
     */
    public static final class Builder {


        private Map<String,GlobMatcher> _bakerTypes;


        /**
         *
         */
        private Builder() {

            _bakerTypes = HashMap.empty();
        }


        /**
         *
         */
        private Builder(final BakerMatcher data) {

            _bakerTypes = HashMap.ofEntries(
                    data._bakerTypes.map(e -> Tuple.of(e.mapType, e.matcher)));
        }


        /**
         *
         */
        public Builder addBakerType(
                final String bakerType,
                final GlobMatcher matcher) {

            _bakerTypes = _bakerTypes.merge(
                    HashMap.of(bakerType, matcher),
                    Builder::extendGlobMatcher);

            return this;
        }


        private static GlobMatcher extendGlobMatcher(
                final GlobMatcher oldMatcher,
                final GlobMatcher newMatcher) {

            return GlobMatcher.builder(oldMatcher)
                    .addGlobMatcher(newMatcher)
                    .build();
        }


        /**
         *
         */
        public Builder addBakerType(
                final String bakerType,
                final String... fileNames) {

            GlobMatcher matcher = new GlobMatcher(fileNames);

            addBakerType(bakerType, matcher);

            return this;
        }


        /**
         *
         */
        public Builder addBakerTypes(final BakerMatcher that) {

            that._bakerTypes.forEach(e -> addBakerType(e.mapType, e.matcher));

            return this;
        }


        /**
         *
         */
        public BakerMatcher build() {

            return new BakerMatcher(this);
        }


    }


}
