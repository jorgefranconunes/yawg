/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.varmateo.yawg.ItemBaker;
import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.PageTemplate;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.util.Exceptions;


/**
 * 
 */
/* package private */ final class DirBaker
        extends Object
        implements ItemBaker {


    private static final String NAME = "dir";

    private final Log _log;
    private final Path _sourceRootDir;
    private final ItemBaker _fileBaker;



    /**
     * @param log The logger that will be used for logging.
     *
     * @param sourceRootDir The top level directory being baked. This
     * is only used to improve logging messages.
     *
     * @param fileBaker Baker to be used on regular files.
     */
    public DirBaker(
            final Log log,
            final Path sourceRootDir,
            final ItemBaker fileBaker) {

        _log = log;
        _sourceRootDir = sourceRootDir;
        _fileBaker = fileBaker;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getShortName() {

        return NAME;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBakeable(final Path path) {

        boolean result = Files.isDirectory(path);

        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void bake(
            final Path sourcePath,
            final Optional<PageTemplate> template,
            final Path targetDir)
            throws YawgException {

        Path sourceRelPath = _sourceRootDir.relativize(sourcePath);
        _log.debug("Baking directory {0}", sourceRelPath);

        if ( !Files.exists(targetDir) ) {
            doIoAction(
                    "create directory",
                    () -> Files.createDirectory(targetDir));
        }

        Path sourceDir = sourcePath;
        List<Path> entries =
                doIoAction(
                        "list directory",
                        ()-> getDirEntries(sourceDir));

        entries.stream()
                .filter(Files::isRegularFile)
                .forEach(path -> _fileBaker.bake(path, template, targetDir));

        entries.stream()
                .filter(Files::isDirectory)
                .forEach(path -> {
                        Path dirBasename = path.getFileName();
                        Path childTargetDir = targetDir.resolve(dirBasename);
                        bake(path, template, childTargetDir);
                    });
    }


    /**
     *
     */
    private List<Path> getDirEntries(final Path dir)
        throws IOException {

        List<Path> result = null;

        try ( Stream<Path> entries = Files.list(dir) ) {
            result = entries.sorted().collect(Collectors.toList());
        }

        return result;
    }


    /**
     *
     */
    private <T> T doIoAction(
            final String description,
            final IoSupplier<T> supplier)
            throws YawgException {

        T result = null;

        try {
            result = supplier.get();
        } catch ( IOException e ) {
            Exceptions.raise(
                    YawgException.class,
                    e,
                    "Failed to {0} - {1} - {2}",
                    description,
                    e.getClass().getSimpleName(),
                    e.getMessage());
        }

        return result;
    }


    /**
     *
     */
    @FunctionalInterface
    private interface IoSupplier<T> {


        /**
         *
         */
        T get() throws IOException;
    }


}
