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

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ListAssert;


/**
 *
 */
public final class PathAssert
        extends AbstractAssert<PathAssert, Path> {


    /**
     *
     */
    public static PathAssert assertThat(final Path actual) {

        return new PathAssert(actual);
    }


    /**
     *
     */
    public PathAssert(final Path actual) {

        super(actual, PathAssert.class);
    }


    /**
     *
     */
    public PathAssert isEmptyDirectory()
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
    public ListAssert<String> sortedEntries()
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
