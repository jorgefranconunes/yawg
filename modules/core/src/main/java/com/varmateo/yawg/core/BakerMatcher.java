/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

import com.varmateo.yawg.util.GlobMatcher;


/**
 *
 */
/* package private */ final class BakerMatcher
        extends Object {


    private Collection<Entry> _bakerTypes;


    /**
     *
     */
    private BakerMatcher(final Builder builder) {

        _bakerTypes =
                builder._bakerTypes.entrySet().stream()
                .map(e -> new Entry(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }


    /**
     * Creates a new builder with no initializations.
     *
     * @return A newly created <code>Builder</code> instance.
     */
    public static Builder builder() {

        Builder result = new Builder();

        return result;
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

        Builder result = new Builder(data);

        return result;
    }


    /**
     *
     */
    public Optional<String> getBakerTypeFor(final Path path) {

        Optional<String> bakerType =
                _bakerTypes.stream()
                .filter(entry -> entry.matcher.test(path))
                .map(entry -> entry.mapType)
                .findFirst();

        return bakerType;
    }


    /**
     * Intended for logging or debugging.
     *
     * @return The string representation of our matchers.
     */
    @Override
    public String toString() {

        String result =
                _bakerTypes.stream()
                .sorted((o1, o2) -> o1.mapType.compareTo(o2.mapType))
                .map(e -> e.mapType + ":" + e.matcher.toString())
                .collect(Collectors.joining(";"));

        return result;
    }


    /**
     *
     */
    private static final class Entry
            extends Object {


        public final String mapType;

        public final GlobMatcher matcher;


        /**
         *
         */
        public Entry(
                final String mapType,
                final GlobMatcher matcher) {

            this.mapType = mapType;
            this.matcher = matcher;
        }


    }


    /**
     *
     */
    public static final class Builder
            extends Object {


        private final Map<String,GlobMatcher> _bakerTypes;


        /**
         *
         */
        private Builder() {

            _bakerTypes = new LinkedHashMap<>();
        }


        /**
         *
         */
        private Builder(final BakerMatcher data) {

            _bakerTypes = new LinkedHashMap<>();
            data._bakerTypes
                    .stream()
                    .forEach(e -> _bakerTypes.put(e.mapType, e.matcher));
        }


        /**
         *
         */
        public Builder addBakerType(
                final String bakerType,
                final GlobMatcher matcher) {

            _bakerTypes.merge(
                    bakerType,
                    matcher,
                    (oldMatcher, newMatcher) ->
                    GlobMatcher.builder(oldMatcher)
                    .addGlobMatcher(newMatcher)
                    .build());

            return this;
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

            that._bakerTypes
                    .stream()
                    .forEach(e -> addBakerType(e.mapType, e.matcher));

            return this;
        }


        /**
         *
         */
        public BakerMatcher build() {

            BakerMatcher result = new BakerMatcher(this);

            return result;
        }


    }


}
