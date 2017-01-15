/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javaslang.collection.List;
import javaslang.collection.Seq;

import com.varmateo.yawg.core.DirBakerConf;
import com.varmateo.yawg.core.DirEntryChecker;


/**
 * Used for obtaining the list of entries in a given directory that
 * should be baked.
 */
/* package private */ final class DirEntryScanner {


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
    public Seq<Path> getDirEntries(final Path dirPath)
            throws IOException {

        final Seq<Path> result;

        try ( Stream<Path> entries = Files.list(dirPath) ) {
            Stream<Path> stream =
                    entries
                    .filter(_entryFilter)
                    .sorted();
            result = List.ofAll(new Iterable<Path>() {
                    @Override
                    public Iterator<Path> iterator() {
                        return stream.iterator();
                    }
                });
        }

        return result;
    }


}
