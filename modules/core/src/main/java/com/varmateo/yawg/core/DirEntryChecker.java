/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

import com.varmateo.yawg.core.DirBakerConf;


/**
 * Used for checking if a file should be used in a baking.
 */
/* package private */ final class DirEntryChecker {


    private final DirBakerConf _conf;
    private final boolean _isIncludeHere;


    /**
     *
     */
    DirEntryChecker(final DirBakerConf conf) {

        _conf = conf;
        _isIncludeHere = conf.filesToIncludeHere.isPresent();
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
                _isIncludeHere
                ? testForIncludeHere(path)
                : !testForExclude(path);

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
    private boolean testForIncludeHere(final Path path) {

        boolean result =
                _conf.filesToIncludeHere
                .map(matcher -> matcher.test(path))
                .orElse(false);

        return result;
    }


    /**
     *
     */
    private boolean testForExclude(final Path path) {

        boolean result =
                _conf.filesToExclude
                .map(matcher -> matcher.test(path))
                .orElse(false);

        return result;
    }


}
