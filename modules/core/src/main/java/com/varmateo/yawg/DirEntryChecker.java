/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Path;
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

        String basename = path.getFileName().toString();
        boolean result = testString(basename);

        return result;
    }


    /**
     *
     */
    private boolean testString(final String name) {

        boolean result =
                _isIncludeOnly
                ? testForIncludeOnly(name)
                : !testForIgnore(name);

        return result;
    }


    /**
     *
     */
    private boolean testForIncludeOnly(final String name) {

        boolean result =
                _conf.filesToIncludeOnly
                .map(
                        collection ->
                        collection.stream()
                        .filter(pattern -> pattern.matcher(name).matches())
                        .findFirst()
                        .isPresent())
                .orElse(false);

        return result;
    }


    /**
     *
     */
    private boolean testForIgnore(final String name) {

        boolean result =
                _conf.filesToIgnore.stream()
                .filter(pattern -> pattern.matcher(name).matches())
                .findFirst()
                .isPresent();

        return result;
    }


}