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


    private final String _toString;
    private final List<PathMatcher> _matchers;


    /**
     * @param globPatterns The glob patterns for this matcher.
     *
     * @throws PatternSyntaxException If any of the given glob
     * patterns is invalid.
     */
    public GlobMatcher(final Collection<String> globPatterns)
            throws PatternSyntaxException {

        _toString = String.join("; ", globPatterns);
        _matchers =
                Lists.map(
                        globPatterns,
                        p -> DEFAULT_FILESYSTEM.getPathMatcher("glob:" + p));
    }


    /**
     *
     */
    private GlobMatcher(final Builder builder) {

        _toString = String.join("; ", builder._globPatterns);
        _matchers = Lists.readOnlyCopy(builder._matchers);
    }


    /**
     *
     */
    private GlobMatcher(
            final String toString,
            final List<PathMatcher> matchers) {

        _toString = toString;
        _matchers = matchers;
    }


    /**
     * Creates a new <code>GlobMatcher</code> resulting from joining
     * all the globa patterns we have the patterns from the given
     * matcher.
     *
     * @param that The matcher to be added to our own.
     *
     * @return A new matcher object obtained from joining our matcher
     * with the given matcher.
     */
    public GlobMatcher add(final GlobMatcher that) {

        String toString = String.join("; ", this._toString, that._toString);
        List<PathMatcher> matchers = new ArrayList<>();

        matchers.addAll(this._matchers);
        matchers.addAll(that._matchers);

        GlobMatcher result = new GlobMatcher(toString, matchers);

        return result;
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

        return _toString;
    }





    /**
     *
     */
    public static final class Builder
            extends Object {


        private final List<String> _globPatterns = new ArrayList<>();
        private final List<PathMatcher> _matchers = new ArrayList<>();


        /**
         *
         */
        public Builder() {
            // Nothing to do.
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
        public GlobMatcher build() {

            GlobMatcher result = new GlobMatcher(this);

            return result;
        }


    }


}
