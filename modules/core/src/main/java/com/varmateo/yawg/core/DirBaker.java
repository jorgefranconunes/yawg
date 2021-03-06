/**************************************************************************
 *
 * Copyright (c) 2016-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

import io.vavr.collection.Seq;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.logging.LogFactory;
import com.varmateo.yawg.spi.DirBakeListener;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.TemplateService;


/**
 * Bakes the files contained in a directory, and recursively bake all
 * sub-directories.
 */
/* default */ final class DirBaker {


    private final Log _log;
    private final FileBaker _fileBaker;
    private final DirBakeOptionsDao _dirBakeOptionsDao;
    private final DirBakeListener _listener;
    private final TemplateService _templateService;


    /**
     * @param log Used for logging.
     *
     * @param fileBaker Baker to be used on regular files.
     *
     * @param templateService Will provide the templates used in the
     * baking of individual files.
     *
     * @param dirBakeOptionsDao Used for reading the bake configuration
     * for each directory.
     *
     * @param dirBakeListener Will be notified when the bake of a
     * directory starts.
     */
    /* default */ DirBaker(
            final FileBaker fileBaker,
            final TemplateService templateService,
            final DirBakeOptionsDao dirBakeOptionsDao,
            final DirBakeListener dirBakeListener) {

        _log = LogFactory.createFor(DirBaker.class);
        _fileBaker = fileBaker;
        _dirBakeOptionsDao = dirBakeOptionsDao;
        _listener = dirBakeListener;
        _templateService = templateService;
    }


    /**
     * 
     */
    public void bakeDirectory(
            final Path sourceDir,
            final Path targetDir,
            final DirBakeOptions parentDirBakeOptions) {

        final DirBakerContext context = DirBakerContext.create(
                sourceDir, targetDir, _templateService, parentDirBakeOptions);

        doBakeDirectory(sourceDir, targetDir, context);
    }


    /**
     * 
     */
    private void doBakeDirectory(
            final Path sourceDir,
            final Path targetDir,
            final DirBakerContext parentContext) {

        final Path relSourceDir = parentContext.sourceRootDir().relativize(sourceDir);
        _log.debug("Baking directory {0}", relSourceDir);

        createDirIfNeeded(targetDir);

        final DirBakeOptions specificDirBakeOptions = _dirBakeOptionsDao.loadFromDir(sourceDir);
        final DirBakerContext context = parentContext.buildForChildDir(
                targetDir,
                specificDirBakeOptions,
                x -> _listener.onDirBake(x).pageVars());
        final DirBakeOptions dirBakeOptions = context.dirBakeOptions();
        final Seq<Path> dirEntries = getDirEntries(sourceDir, dirBakeOptions);

        bakeChildFiles(dirEntries, targetDir, dirBakeOptions, context.pageContext());
        bakeChildDirectories(dirEntries, targetDir, context);
        bakeExtraDirectories(sourceDir, targetDir, context);
    }


    /**
     *
     */
    private void createDirIfNeeded(final Path targetDir) {

        if ( !Files.exists(targetDir) ) {
            doIoAction(
                    () -> Files.createDirectory(targetDir),
                    DirBakerException.directoryCreationFailure(targetDir));
        }
    }


    /**
     *
     */
    private Seq<Path> getDirEntries(
            final Path dir,
            final DirBakeOptions dirBakeOptions) {

        final DirEntryScanner scanner = new DirEntryScanner(dirBakeOptions);

        return doIoAction(
                () -> scanner.getDirEntries(dir),
                DirBakerException.directoryListFailure(dir));
    }


    /**
     *
     */
    private void bakeChildFiles(
            final Seq<Path> dirEntries,
            final Path targetDir,
            final DirBakeOptions dirBakeOptions,
            final PageContext context) {

        final Seq<Path> filePathList = dirEntries.filter(Files::isRegularFile);

        for ( final Path path : filePathList ) {
            _fileBaker.bakeFile(path, context, targetDir, dirBakeOptions);
        }
    }


    /**
     *
     */
    private void bakeChildDirectories(
            final Seq<Path> dirEntries,
            final Path targetDir,
            final DirBakerContext context) {

        final Seq<Path> dirPathList = dirEntries.filter(Files::isDirectory);

        for ( final Path childSourceDir : dirPathList ) {
            final Path dirBasename = childSourceDir.getFileName();
            final Path childTargetDir = targetDir.resolve(dirBasename);

            doBakeDirectory(childSourceDir, childTargetDir, context);
        }
    }


    /**
     *
     */
    private void bakeExtraDirectories(
            final Path sourceDir,
            final Path targetDir,
            final DirBakerContext context) {

        final Seq<Path> extraDirPathList = context.dirBakeOptions()
                .extraDirsHere
                .map(path -> sourceDir.resolve(path));

        for ( final Path extraSourceDir : extraDirPathList ) {
            doBakeDirectory(extraSourceDir, targetDir, context);
        }
    }


    /**
     * A simple wrapper to change a method signature from the checked
     * IOException to the unchecked YawgException.
     */
    private <T> T doIoAction(
            final IoSupplier<T> supplier,
            final Function<IOException, DirBakerException> exceptionBuilder) {

        final T result;

        try {
            result = supplier.get();
        } catch ( IOException e ) {
            throw exceptionBuilder.apply(e);
        }

        return result;
    }


    /**
     * Utility interface to simplify creating lambdas whose body may
     * throw exceptions.
     *
     * @param <T> Type of the values returned by the function.
     */
    @FunctionalInterface
    private interface IoSupplier<T> {


        /**
         *
         */
        T get() throws IOException;
    }


}
