/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.function.Predicate;
import java.util.regex.PatternSyntaxException;

import io.vavr.collection.Array;
import io.vavr.collection.Seq;


/**
 * A predicate for checking if a path matches a given collection of
 * glob patterns.
 */
public final class GlobMatcher
        implements Predicate<Path> {


    private static final FileSystem DEFAULT_FILESYSTEM =
            FileSystems.getDefault();


    private final Seq<String> _globPatterns;
    private final Seq<PathMatcher> _matchers;


    /**
     * @param globPatterns The glob patterns for this matcher.
     *
     * @throws PatternSyntaxException If any of the given glob
     * patterns is invalid.
     */
    public GlobMatcher(final Seq<String> globPatterns)
            throws PatternSyntaxException {

        _globPatterns = globPatterns;
        _matchers = globPatterns
                .map(p -> "glob:" + p)
                .map(DEFAULT_FILESYSTEM::getPathMatcher);
    }


    /**
     *
     */
    public GlobMatcher(final String... globPatterns)
            throws PatternSyntaxException {

        this(Array.of(globPatterns));
    }


    /**
     *
     */
    private GlobMatcher(final Builder builder) {

        _globPatterns = builder._globPatterns;
        _matchers = builder._matchers;
    }


    /**
     *
     */
    private GlobMatcher(
            final Seq<String> globPatterns,
            final Seq<PathMatcher> matchers) {

        _globPatterns = globPatterns;
        _matchers = matchers;
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
     * <code>GlobMatcher</code>.
     *
     * @param data Used for initializing the builder state.
     *
     * @return A newly created <code>Builder</code> instance.
     */
    public static Builder builder(final GlobMatcher data) {

        return new Builder(data);
    }


    /**
     * Checks if the given path matches the glob pattern represented
     * by this object.
     *
     * @param path The path to be checked.
     *
     * @return True if the given path matches our glob pattern. False
     * otherwise.
     */
    @Override
    public boolean test(final Path path) {

        Path name = path.getFileName();
        Predicate<PathMatcher> isMatch = matcher -> matcher.matches(name);

        return _matchers
                .filter(isMatch)
                .headOption()
                .isDefined();
    }


    /**
     * Intended for logging or debugging.
     *
     * @return The string representation of our glob pattern.
     */
    @Override
    public String toString() {

        // This implementation is not particularly performant. Let us
        // hope client code only uses this method for sporadic logging
        // or debugging.

        return String.join(",", _globPatterns);
    }





    /**
     * A builder of <code>GlobMatcher</code> instances.
     */
    public static final class Builder {


        private Seq<String> _globPatterns;
        private Seq<PathMatcher> _matchers;


        /**
         *
         */
        private Builder() {

            _globPatterns = Array.of();
            _matchers = Array.of();
        }


        /**
         *
         */
        private Builder(final GlobMatcher globMatcher) {

            _globPatterns = globMatcher._globPatterns;
            _matchers = globMatcher._matchers;
        }


        /**
         *
         *
         * @throws PatternSyntaxException If the given glob pattern is
         * invalid.
         */
        public Builder addGlobPattern(final String globPattern) {

            PathMatcher matcher =
                    DEFAULT_FILESYSTEM.getPathMatcher("glob:" + globPattern);

            _globPatterns = _globPatterns.append(globPattern);
            _matchers = _matchers.append(matcher);

            return this;
        }


        /**
         *
         */
        public Builder addGlobMatcher(final GlobMatcher globMatcher) {

            _globPatterns = _globPatterns.appendAll(globMatcher._globPatterns);
            _matchers = _matchers.appendAll(globMatcher._matchers);

            return this;
        }


        /**
         *
         */
        public GlobMatcher build() {

            return new GlobMatcher(this);
        }


    }


}
