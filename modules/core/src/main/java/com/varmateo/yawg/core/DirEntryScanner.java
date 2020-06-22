/**************************************************************************
 *
 * Copyright (c) 2016-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.stream.Stream;

import io.vavr.collection.List;
import io.vavr.collection.Seq;


/**
 * Used for obtaining the list of entries in a given directory that
 * should be baked.
 */
/* default */ final class DirEntryScanner {


    private final Predicate<Path> _entryFilter;


    /**
     *
     */
    /* default */ DirEntryScanner(final DirBakeOptions options) {

        _entryFilter = new DirEntryChecker(options).asPathPredicate();
    }


    /**
     *
     */
    public Seq<Path> getDirEntries(final Path dirPath)
            throws IOException {

        try ( Stream<Path> entries = Files.list(dirPath) ) {
            final Stream<Path> stream =
                    entries
                    .filter(_entryFilter)
                    .sorted();
            return List.ofAll(() -> stream.iterator());
        }
    }


}
