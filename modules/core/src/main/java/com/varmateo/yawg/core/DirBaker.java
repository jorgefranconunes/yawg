/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.varmateo.yawg.DirBakeListener;
import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.Template;
import com.varmateo.yawg.TemplateService;
import com.varmateo.yawg.PageVars;
import com.varmateo.yawg.YawgException;

import com.varmateo.yawg.core.DirBakerConf;
import com.varmateo.yawg.core.DirBakerConfDao;
import com.varmateo.yawg.core.DirEntryScanner;
import com.varmateo.yawg.core.FileBaker;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.logging.LogWithUtils;
import com.varmateo.yawg.util.Exceptions;


/**
 * Bakes the files contained in a directory, and recursively bake all
 * sub-directories.
 */
/* package private */ final class DirBaker
        extends Object {


    private final LogWithUtils _log;
    private final Path _sourceRootDir;
    private final FileBaker _fileBaker;
    private final Optional<TemplateService> _templateService;
    private final DirBakerConfDao _dirBakerConfDao;
    private final DirBakeListener _listener;



    /**
     * @param log Used for logging.
     *
     * @param sourceRootDir The top level directory being baked. This
     * is only used to improve logging messages.
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
    public DirBaker(
            final Log log,
            final Path sourceRootDir,
            final FileBaker fileBaker,
            final Optional<TemplateService> templateService,
            final DirBakerConfDao dirBakerConfDao,
            final DirBakeListener dirBakeListener) {

        _log = LogWithUtils.from(log);
        _sourceRootDir = sourceRootDir;
        _fileBaker = fileBaker;
        _templateService = templateService;
        _dirBakerConfDao = dirBakerConfDao;
        _listener = dirBakeListener;
    }


    /**
     * 
     */
    public void bakeDirectory(
            final Path sourceDir,
            final Path targetDir,
            final DirBakerConf parentDirBakerConf)
            throws YawgException {

        PageVars pageVars = new PageVars();

        doBakeDirectory(sourceDir, targetDir, parentDirBakerConf, pageVars);
    }


    /**
     * 
     */
    private void doBakeDirectory(
            final Path sourceDir,
            final Path targetDir,
            final DirBakerConf parentDirBakerConf,
            final PageVars pageVars)
            throws YawgException {

        Path relSourceDir = _sourceRootDir.relativize(sourceDir);
        _log.debug("Baking directory {0}", relSourceDir);

        createDirIfNeeded(targetDir);

        DirBakerConf dirBakerConf =
                _dirBakerConfDao
                .loadFromDir(sourceDir)
                .mergeOnTopOf(parentDirBakerConf);

        List<Path> dirEntries = getDirEntries(sourceDir, dirBakerConf);

        List<Path> filePathList =
                dirEntries.stream()
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());

        List<Path> dirPathList =
                dirEntries.stream()
                .filter(Files::isDirectory)
                .collect(Collectors.toList());

        PageContext context =
                buildPageContext(sourceDir, dirBakerConf, pageVars);

        bakeChildFiles(filePathList, targetDir, dirBakerConf, context);
        bakeChildDirectories(dirPathList, targetDir, dirBakerConf, context);
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
    private List<Path> getDirEntries(
            final Path dir,
            final DirBakerConf dirBakerConf) {

        DirEntryScanner scanner = new DirEntryScanner(dirBakerConf);
        List<Path> result =
                doIoAction(
                        "list directory",
                        () -> scanner.getDirEntries(dir));

        return result;
    }


    /**
     *
     */
    private PageContext buildPageContext(
            final Path sourceDir,
            final DirBakerConf dirBakerConf,
            final PageVars vars)
            throws YawgException {

        Optional<Template> template =
                dirBakerConf.templateName
                .flatMap(name ->
                         _templateService.flatMap(
                                 srv -> srv.getTemplate(name)));
        String dirUrl = buildRelativeUrl(sourceDir, _sourceRootDir);
        String rootRelativeUrl = buildRelativeUrl(_sourceRootDir, sourceDir);
        PageVars newVars =
                PageVars.builder(vars)
                .addPageVars(dirBakerConf.pageVars)
                .build();
        PageContext context =
                PageContext.builder()
                .setDirUrl(dirUrl)
                .setRootRelativeUrl(rootRelativeUrl)
                .setTemplate(template)
                .setPageVars(newVars)
                .build();
        PageVars extendedVars = _listener.onDirBake(context);
        PageContext extendedContext =
                PageContext.builder(context)
                .setPageVars(extendedVars)
                .build();

        return extendedContext;
    }


    /**
     * Generates the URL for the given <code>dir</code> relative to
     * the given <code>baseDir</code>.
     */
    private String buildRelativeUrl(
            final Path dir,
            final Path baseDir) {

        Path relDir = baseDir.relativize(dir);
        String result = relDir.toString().replace(File.separatorChar, '/');

        if ( result.length() == 0 ) {
            result = ".";
        }

        return result;
    }


    /**
     *
     */
    private void bakeChildFiles(
            final List<Path> filePathList,
            final Path targetDir,
            final DirBakerConf dirBakerConf,
            final PageContext context)
            throws YawgException {

        for ( Path path : filePathList ) {
            _fileBaker.bakeFile(path, context, targetDir, dirBakerConf);
        }
    }


    /**
     *
     */
    private void bakeChildDirectories(
            final List<Path> dirPathList,
            final Path targetDir,
            final DirBakerConf dirBakerConf,
            final PageContext context) {

        PageVars vars = context.getPageVars();

        for ( Path childSourceDir : dirPathList ) {
            Path dirBasename = childSourceDir.getFileName();
            Path childTargetDir = targetDir.resolve(dirBasename);
            doBakeDirectory(childSourceDir, childTargetDir, dirBakerConf, vars);
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
