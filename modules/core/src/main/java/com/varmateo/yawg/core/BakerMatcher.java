/**************************************************************************
 *
 * Copyright (c) 2016-2020 Yawg project contributors.
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


    private final Seq<Entry> _bakerTypes;


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
    public Option<String> bakerTypeFor(final Path path) {

        return _bakerTypes
                .filter(entry -> entry.matcher().test(path))
                .map(Entry::mapType)
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
                .sorted((o1, o2) -> o1.mapType().compareTo(o2.mapType()))
                .map(e -> e.mapType() + ":" + e.matcher().toString())
                .mkString(";");
    }


    /**
     *
     */
    private static final class Entry {


        private final String _mapType;
        private final GlobMatcher _matcher;


        /**
         *
         */
        /* default */ Entry(
                final String mapType,
                final GlobMatcher matcher) {

            _mapType = mapType;
            _matcher = matcher;
        }


        /**
         *
         */
        public String mapType() {

            return _mapType;
        }


        /**
         *
         */
        public GlobMatcher matcher() {

            return _matcher;
        }


    }


    /**
     *
     */
    public static final class Builder {


        private Map<String, GlobMatcher> _bakerTypes;


        /**
         *
         */
        /* default */ Builder() {

            _bakerTypes = HashMap.empty();
        }


        /**
         *
         */
        /* default */ Builder(final BakerMatcher data) {

            _bakerTypes = HashMap.ofEntries(
                    data._bakerTypes.map(e -> Tuple.of(e.mapType(), e.matcher())));
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

            final GlobMatcher matcher = GlobMatcher.create(fileNames);

            addBakerType(bakerType, matcher);

            return this;
        }


        /**
         *
         */
        public Builder addBakerTypes(final BakerMatcher that) {

            that._bakerTypes.forEach(e -> addBakerType(e.mapType(), e.matcher()));

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
