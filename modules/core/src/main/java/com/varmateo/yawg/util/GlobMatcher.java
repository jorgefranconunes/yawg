/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.util;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.PatternSyntaxException;

import com.varmateo.yawg.util.Lists;





/**
 * A predicate for checking if a path matches a given collection of
 * glob patterns.
 */
public final class GlobMatcher
        extends Object
        implements Predicate<Path> {


    private static final FileSystem DEFAULT_FILESYSTEM =
            FileSystems.getDefault();


    private final List<String> _globPatterns;
    private final List<PathMatcher> _matchers;


    /**
     * @param globPatterns The glob patterns for this matcher.
     *
     * @throws PatternSyntaxException If any of the given glob
     * patterns is invalid.
     */
    public GlobMatcher(final Collection<String> globPatterns)
            throws PatternSyntaxException {

        _globPatterns = Lists.readOnlyCopy(globPatterns);
        _matchers =
                Lists.map(
                        globPatterns,
                        p -> DEFAULT_FILESYSTEM.getPathMatcher("glob:" + p));
    }


    /**
     *
     */
    private GlobMatcher(final Builder builder) {

        _globPatterns = Lists.readOnlyCopy(builder._globPatterns);
        _matchers = Lists.readOnlyCopy(builder._matchers);
    }


    /**
     *
     */
    private GlobMatcher(
            final List<String> globPatterns,
            final List<PathMatcher> matchers) {

        _globPatterns = globPatterns;
        _matchers = matchers;
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
        boolean result =
                _matchers.stream()
                .filter(matcher -> matcher.matches(name))
                .findFirst()
                .isPresent();

        return result;
    }


    /**
     * @return The string representation of our glob pattern. This is
     * the same string passed to the constructor.
     */
    @Override
    public String toString() {

        // This implementation is not particularly performant. Let's
        // hope client code only uses this method for sporadic logging
        // or debugging.

        String result = String.join(";", _globPatterns);

        return result;
    }





    /**
     *
     */
    public static final class Builder
            extends Object {


        private final List<String> _globPatterns;
        private final List<PathMatcher> _matchers;


        /**
         *
         */
        public Builder() {

            _globPatterns = new ArrayList<>();
            _matchers = new ArrayList<>();
        }


        /**
         *
         */
        public Builder(final GlobMatcher globMatcher) {

            _globPatterns = new ArrayList<>(globMatcher._globPatterns);
            _matchers = new ArrayList<>(globMatcher._matchers);
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

            _globPatterns.add(globPattern);
            _matchers.add(matcher);

            return this;
        }


        /**
         *
         */
        public Builder addGlobMatcher(final GlobMatcher globMatcher) {

            _globPatterns.addAll(globMatcher._globPatterns);
            _matchers.addAll(globMatcher._matchers);

            return this;
        }


        /**
         *
         */
        public GlobMatcher build() {

            GlobMatcher result = new GlobMatcher(this);

            return result;
        }


    }


}
