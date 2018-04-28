/**************************************************************************
 *
 * Copyright (c) 2016-2018 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.core.DirBakerConf;
import com.varmateo.yawg.core.DirBakerConfDao;
import com.varmateo.yawg.core.DirEntryScanner;
import com.varmateo.yawg.core.DirPageContextBuilder;
import com.varmateo.yawg.core.FileBaker;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.spi.DirBakeListener;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.PageContextBuilder;
import com.varmateo.yawg.spi.PageVars;
import com.varmateo.yawg.spi.PageVarsBuilder;
import com.varmateo.yawg.spi.TemplateService;
import com.varmateo.yawg.util.Exceptions;
import io.vavr.collection.Seq;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * Bakes the files contained in a directory, and recursively bake all
 * sub-directories.
 */
/* package private */ final class DirBaker {


    private final Log _log;
    private final Path _sourceRootDir;
    private final FileBaker _fileBaker;
    private final DirBakerConfDao _dirBakerConfDao;
    private final DirBakeListener _listener;
    private final DirPageContextBuilder _dirPageContextBuilder;



    /**
     * @param log Used for logging.
     *
     * @param sourceRootDir The top level directory being baked. This
     * is only used to improve logging messages.
     *
     * @param targetRootDir The top level directory where the bake
     * results are stored.
     *
     * @param fileBaker Baker to be used on regular files.
     *
     * @param templateService Will provide the templates used in the
     * baking of individual files.
     *
     * @param dirBakerConfDao Used for reading the bake configuration
     * for each directory.
     *
     * @param dirBakeListener Will be notified when the bake of a
     * directory starts.
     */
    DirBaker(
            final Log log,
            final Path sourceRootDir,
            final Path targetRootDir,
            final FileBaker fileBaker,
            final TemplateService templateService,
            final DirBakerConfDao dirBakerConfDao,
            final DirBakeListener dirBakeListener) {

        _log = log;
        _sourceRootDir = sourceRootDir;
        _fileBaker = fileBaker;
        _dirBakerConfDao = dirBakerConfDao;
        _listener = dirBakeListener;
        _dirPageContextBuilder =
                new DirPageContextBuilder(targetRootDir, templateService);
    }


    /**
     * 
     */
    public void bakeDirectory(
            final Path sourceDir,
            final Path targetDir,
            final DirBakerConf parentDirBakerConf)
            throws YawgException {

        PageVars parentExtensionVars = PageVars.empty();

        doBakeDirectory(
                sourceDir,
                targetDir,
                parentDirBakerConf,
                parentExtensionVars);
    }


    /**
     * 
     */
    private void doBakeDirectory(
            final Path sourceDir,
            final Path targetDir,
            final DirBakerConf parentDirBakerConf,
            final PageVars parentExtensionVars)
            throws YawgException {

        Path relSourceDir = _sourceRootDir.relativize(sourceDir);
        _log.debug("Baking directory {0}", relSourceDir);

        createDirIfNeeded(targetDir);

        DirBakerConf dirBakerConf =
                _dirBakerConfDao
                .loadFromDir(sourceDir)
                .mergeOnTopOf(parentDirBakerConf);
        PageContext context =
                _dirPageContextBuilder.buildPageContext(
                        targetDir,
                        dirBakerConf,
                        parentExtensionVars);
        PageVars thisDirExtensionVars = _listener.onDirBake(context);
        PageVars extensionVars =
                PageVarsBuilder.create(parentExtensionVars)
                .addPageVars(thisDirExtensionVars)
                .build();
        PageContext extendedContext =
                PageContextBuilder.create(context)
                .addPageVars(extensionVars)
                .build();
        Seq<Path> dirEntries = getDirEntries(sourceDir, dirBakerConf);

        bakeChildFiles(dirEntries, targetDir, dirBakerConf, extendedContext);
        bakeChildDirectories(dirEntries, targetDir, dirBakerConf,extensionVars);
        bakeExtraDirectories(
                sourceDir,
                targetDir,
                dirBakerConf,
                extensionVars);
    }


    /**
     *
     */
    private void createDirIfNeeded(final Path targetDir) {

        if ( !Files.exists(targetDir) ) {
            doIoAction(
                    "create directory",
                    () -> Files.createDirectory(targetDir));
        }
    }


    /**
     *
     */
    private Seq<Path> getDirEntries(
            final Path dir,
            final DirBakerConf dirBakerConf) {

        DirEntryScanner scanner = new DirEntryScanner(dirBakerConf);

        return doIoAction(
                "list directory",
                () -> scanner.getDirEntries(dir));
    }


    /**
     *
     */
    private void bakeChildFiles(
            final Seq<Path> dirEntries,
            final Path targetDir,
            final DirBakerConf dirBakerConf,
            final PageContext context)
            throws YawgException {

        Seq<Path> filePathList = dirEntries.filter(Files::isRegularFile);

        for ( Path path : filePathList ) {
            _fileBaker.bakeFile(path, context, targetDir, dirBakerConf);
        }
    }


    /**
     *
     */
    private void bakeChildDirectories(
            final Seq<Path> dirEntries,
            final Path targetDir,
            final DirBakerConf dirBakerConf,
            final PageVars extensionVars) {

        Seq<Path> dirPathList = dirEntries.filter(Files::isDirectory);

        for ( Path childSourceDir : dirPathList ) {
            Path dirBasename = childSourceDir.getFileName();
            Path childTargetDir = targetDir.resolve(dirBasename);
            doBakeDirectory(
                    childSourceDir,
                    childTargetDir,
                    dirBakerConf,
                    extensionVars);
        }
    }


    /**
     *
     */
    private void bakeExtraDirectories(
            final Path sourceDir,
            final Path targetDir,
            final DirBakerConf dirBakerConf,
            final PageVars extensionVars) {

        Seq<Path> extraDirPathList =
                dirBakerConf.extraDirsHere
                .map(path -> sourceDir.resolve(path));

        for ( Path extraSourceDir : extraDirPathList ) {
            DirBakerConf extraDirBakerConf =
                    _dirBakerConfDao
                    .loadFromDir(extraSourceDir)
                    .mergeOnTopOf(dirBakerConf);
            Seq<Path> dirEntries =
                    getDirEntries(extraSourceDir, extraDirBakerConf);
            PageContext extraContext =
                    _dirPageContextBuilder.buildPageContext(
                            targetDir,
                            extraDirBakerConf,
                            extensionVars);

            bakeChildFiles(
                    dirEntries,
                    targetDir,
                    extraDirBakerConf,
                    extraContext);
            bakeChildDirectories(
                    dirEntries,
                    targetDir,
                    extraDirBakerConf,
                    extensionVars);
        }
    }


    /**
     * A simple wrapper to change a method signature from the checked
     * IOException to the unchecked YawgException.
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
                    e,
                    "Failed to {0} - {1} - {2}",
                    description,
                    e.getClass().getSimpleName(),
                    e.getMessage());
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
