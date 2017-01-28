/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.testutils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.ListAssert;
import org.assertj.core.api.PathAssert;


/**
 * Assertions for paths that are directories.
 */
public final class DirPathAssert
        extends PathAssert {


    /**
     *
     */
    public static DirPathAssert assertThatDir(final Path actual) {

        return new DirPathAssert(actual);
    }


    /**
     *
     */
    public DirPathAssert(final Path actual) {

        super(actual);
    }


    /**
     *
     */
    public DirPathAssert isEmptyDirectory()
            throws IOException {

        isNotNull();

        long entryCount = Files.list(actual).count();

        if ( entryCount != 0 ) {
            failWithMessage(
                    "Expected empty directory \"%s\" but has %d entries",
                    entryCount);
        }

        return this;
    }


    /**
     *
     */
    public ListAssert<String> entryNames()
            throws IOException {

        isNotNull();

        List<String> entries =
                Files.list(actual)
                .map(Path::getFileName)
                .map(Object::toString)
                .collect(Collectors.toList());

        return new ListAssert<String>(entries);
    }


    /**
     *
     */
    public ListAssert<String> sortedEntryNames()
            throws IOException {

        isNotNull();

        List<String> entries =
                Files.list(actual)
                .map(Path::getFileName)
                .map(Object::toString)
                .sorted()
                .collect(Collectors.toList());

        return new ListAssert<String>(entries);
    }


}
