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

import com.varmateo.yawg.DirBakerConf;
import com.varmateo.yawg.DirBakerConfDao;
import com.varmateo.yawg.ItemBaker;
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
    private final ItemBaker _fileBaker;
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
            final ItemBaker fileBaker,
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

        if ( !Files.exists(targetDir) ) {
            doIoAction(
                    "create directory",
                    () -> Files.createDirectory(targetDir));
        }

        DirBakerConf dirBakerConf =
                _dirBakerConfDao
                .load(sourceDir)
                .merge(parentDirBakerConf);

        List<Path> entries =
                doIoAction(
                        "list directory",
                        ()-> getDirEntries(sourceDir, dirBakerConf));

        List<Path> filePathList =
                entries.stream()
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());

        if ( !filePathList.isEmpty() ) {
            bakeChildFiles(filePathList, targetDir, dirBakerConf);
        }

        List<Path> dirPathList =
                entries.stream()
                .filter(Files::isDirectory)
                .collect(Collectors.toList());

        if ( !dirPathList.isEmpty() ) {
            bakeChildDirectories(
                    dirPathList,
                    targetDir,
                    dirBakerConf);
        }
    }


    /**
     *
     */
    private void bakeChildFiles(
            final List<Path> filePathList,
            final Path targetDir,
            final DirBakerConf dirBakerConf)
            throws YawgException {

        Optional<PageTemplate> template =
                dirBakerConf.templateName
                .flatMap(name ->
                         _templateService.map(srv -> srv.getTemplate(name)));

        for ( Path path : filePathList ) {
            _fileBaker.bake(path, template, targetDir);
        }
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
     *
     */
    private List<Path> getDirEntries(
            final Path dir,
            final DirBakerConf dirBakerConf)
        throws IOException {

        List<Path> result = null;

        try ( Stream<Path> entries = Files.list(dir) ) {
            DirEntryChecker checker = new DirEntryChecker(dirBakerConf);
            result =
                    entries
                    .filter(checker.asPathPredicate())
                    .sorted()
                    .collect(Collectors.toList());
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
