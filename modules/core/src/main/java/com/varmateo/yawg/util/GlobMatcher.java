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
import java.util.function.Predicate;
import java.util.regex.PatternSyntaxException;





/**
 * A predicate for checking if a path matches a given glob pattern.
 */
public final class GlobMatcher
        extends Object
        implements Predicate<Path> {


    private static final FileSystem DEFAULT_FILESYSTEM =
            FileSystems.getDefault();


    private final String _globPattern;
    private final PathMatcher _matcher;


    /**
     * @param globPattern The glob pattern for this matcher.
     *
     * @throws PatternSyntaxException If the given glob pattern is
     * invalid.
     */
    public GlobMatcher(final String globPattern)
            throws PatternSyntaxException {

        _globPattern = globPattern;
        _matcher = DEFAULT_FILESYSTEM.getPathMatcher("glob:" + globPattern);
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
        boolean result = _matcher.matches(name);

        return result;
    }


    /**
     * @return The string representation of our glob pattern. This is
     * the same string passed to the constructor.
     */
    @Override
    public String toString() {

        return _globPattern;
    }


}