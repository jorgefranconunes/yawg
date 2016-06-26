/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.varmateo.yawg.DirBakerConf;
import com.varmateo.yawg.DirBakerConfDao;
import com.varmateo.yawg.DirEntryScanner;
import com.varmateo.yawg.FileBaker;
import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.PageTemplate;
import com.varmateo.yawg.PageTemplateService;
import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.logging.LogWithUtils;


/**
 * 
 */
/* package private */ final class DirBaker
        extends Object {


    private final LogWithUtils _log;
    private final Path _sourceRootDir;
    private final FileBaker _fileBaker;
    private final Optional<PageTemplateService> _templateService;
    private final DirBakerConfDao _dirBakerConfDao;



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
     */
    public DirBaker(
            final Log log,
            final Path sourceRootDir,
            final FileBaker fileBaker,
            final Optional<PageTemplateService> templateService,
            final DirBakerConfDao dirBakerConfDao) {

        _log = LogWithUtils.from(log);
        _sourceRootDir = sourceRootDir;
        _fileBaker = fileBaker;
        _templateService = templateService;
        _dirBakerConfDao = dirBakerConfDao;
    }


    /**
     * 
     */
    public void bakeDirectory(
            final DirBakerConf parentDirBakerConf,
            final Path sourceDir,
            final Path targetDir)
            throws YawgException {

        Path relSourceDir = _sourceRootDir.relativize(sourceDir);
        _log.debug("Baking directory {0}", relSourceDir);

        createDirIfNeeded(targetDir);

        DirBakerConf dirBakerConf =
                _dirBakerConfDao
                .loadFromDir(sourceDir)
                .merge(parentDirBakerConf);

        List<Path> dirEntries = getDirEntries(sourceDir, dirBakerConf);

        List<Path> filePathList =
                dirEntries.stream()
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());

        List<Path> dirPathList =
                dirEntries.stream()
                .filter(Files::isDirectory)
                .collect(Collectors.toList());

        bakeChildFiles(sourceDir, filePathList, targetDir, dirBakerConf);
        bakeChildDirectories(dirPathList, targetDir, dirBakerConf);
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

        List<Path> result =
                doIoAction(
                        "list directory",
                        () -> new DirEntryScanner(dirBakerConf).getDirEntries(dir));

        return result;
    }


    /**
     *
     */
    private void bakeChildFiles(
            final Path sourceDir,
            final List<Path> filePathList,
            final Path targetDir,
            final DirBakerConf dirBakerConf)
            throws YawgException {

        Optional<PageTemplate> template =
                dirBakerConf.templateName
                .flatMap(name ->
                         _templateService.map(srv -> srv.getTemplate(name)));
        String rootRelativeUrl = buildRootRelativeUrl(sourceDir);
        PageContext context =
                new PageContext.Builder()
                .setPageTemplate(template)
                .setRootRelativeUrl(rootRelativeUrl)
                .build();

        for ( Path path : filePathList ) {
            _fileBaker.bakeFile(path, context, targetDir, dirBakerConf);
        }
    }


    /**
     *
     */
    private String buildRootRelativeUrl(final Path sourceDir) {

        Path relDir = sourceDir.relativize(_sourceRootDir);
        String result = relDir.toString().replace(File.separatorChar, '/');

        if ( result.length() == 0 ) {
            result = ".";
        }

        return result;
    }


    /**
     *
     */
    private void bakeChildDirectories(
            final List<Path> dirPathList,
            final Path targetDir,
            final DirBakerConf dirBakerConf) {

        for ( Path childSourceDir : dirPathList ) {
            Path dirBasename = childSourceDir.getFileName();
            Path childTargetDir = targetDir.resolve(dirBasename);
            bakeDirectory(dirBakerConf, childSourceDir, childTargetDir);
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
            YawgException.raise(
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
