/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

import com.varmateo.yawg.core.DirBakeOptions;


/**
 * Used for checking if a file should be used in a baking.
 */
/* package private */ final class DirEntryChecker {


    private final DirBakeOptions _options;
    private final boolean _isIncludeHere;


    /**
     *
     */
    DirEntryChecker(final DirBakeOptions options) {

        _options = options;
        _isIncludeHere = options.filesToIncludeHere.isDefined();
    }


    /**
     *
     */
    public Predicate<Path> asPathPredicate() {

        return this::testPath;
    }


    /**
     *
     */
    public Predicate<String> asStringPredicate() {

        return this::testString;
    }


    /**
     *
     */
    private boolean testPath(final Path path) {

        return _isIncludeHere
                ? testForIncludeHere(path)
                : !testForExclude(path);
    }


    /**
     *
     */
    private boolean testString(final String name) {

        Path path = Paths.get(name);

        return testPath(path);
    }


    /**
     *
     */
    private boolean testForIncludeHere(final Path path) {

        return _options.filesToIncludeHere
                .map(matcher -> matcher.test(path))
                .getOrElse(false);
    }


    /**
     *
     */
    private boolean testForExclude(final Path path) {

        return _options.filesToExclude
                .map(matcher -> matcher.test(path))
                .getOrElse(false)
                || _options.filesToExcludeHere
                .map(matcher -> matcher.test(path))
                .getOrElse(false);
    }


}
