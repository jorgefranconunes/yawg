/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.varmateo.yawg.core.DirBakerConf;
import com.varmateo.yawg.core.DirEntryChecker;


/**
 * Used for obtaining the list of entries in a given directory that
 * should be baked.
 */
/* package private */ final class DirEntryScanner
        extends Object {


    private final Predicate<Path> _entryFilter;


    /**
     *
     */
    DirEntryScanner(final DirBakerConf conf) {

        _entryFilter = new DirEntryChecker(conf).asPathPredicate();
    }


    /**
     *
     */
    public List<Path> getDirEntries(final Path dirPath)
            throws IOException {

        List<Path> result = null;

        try ( Stream<Path> entries = Files.list(dirPath) ) {
            result =
                    entries
                    .filter(_entryFilter)
                    .sorted()
                    .collect(Collectors.toList());
        }

        return result;
    }


}
