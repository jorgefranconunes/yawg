/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

import com.varmateo.yawg.DirBakerConf;


/**
 * Used for checking if a file should be used in a baking.
 */
/* package private */ final class DirEntryChecker
        extends Object {


    private final DirBakerConf _conf;
    private final boolean _isIncludeOnly;


    /**
     *
     */
    public DirEntryChecker(final DirBakerConf conf) {

        _conf = conf;
        _isIncludeOnly = conf.filesToIncludeOnly.isPresent();
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

        boolean result =
                _isIncludeOnly
                ? testForIncludeOnly(path)
                : !testForIgnore(path);

        return result;
    }


    /**
     *
     */
    private boolean testString(final String name) {

        Path path = Paths.get(name);
        boolean result = testPath(path);

        return result;
    }


    /**
     *
     */
    private boolean testForIncludeOnly(final Path path) {

        boolean result =
                _conf.filesToIncludeOnly
                .map(matcher -> matcher.test(path))
                .orElse(false);

        return result;
    }


    /**
     *
     */
    private boolean testForIgnore(final Path path) {

        boolean result =
                _conf.filesToIgnore
                .map(matcher -> matcher.test(path))
                .orElse(false);

        return result;
    }


}
