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
import com.varmateo.yawg.ItemBaker;
import com.varmateo.yawg.PageTemplate;
import com.varmateo.yawg.PageTemplateService;
import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.logging.Log;


/**
 * 
 */
/* package private */ final class DirBaker
        extends Object {


    private final Log _log;
    private final Path _sourceRootDir;
    private final ItemBaker _fileBaker;
    private final PageTemplateService _templateService;



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
     */
    public DirBaker(
            final Log log,
            final Path sourceRootDir,
            final ItemBaker fileBaker,
            final PageTemplateService templateService) {

        _log = log;
        _sourceRootDir = sourceRootDir;
        _fileBaker = fileBaker;
        _templateService = templateService;
    }


    /**
     * 
     */
    public void bakeDirectory(
            final Path sourceDir,
            final Path targetDir,
            final DirBakerConf parentDirBakerConf)
            throws YawgException {

        Path relSourceDir = _sourceRootDir.relativize(sourceDir);
        _log.debug("Baking directory {0}", relSourceDir);

        if ( !Files.exists(targetDir) ) {
            doIoAction(
                    "create directory",
                    () -> Files.createDirectory(targetDir));
        }

        DirBakerConf dirBakerConf =
                buildDirBakerConf(parentDirBakerConf, sourceDir);

        List<Path> entries =
                doIoAction(
                        "list directory",
                        ()-> getDirEntries(sourceDir));

        List<Path> filePathList =
                entries.stream()
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());

        if ( filePathList.size() > 0 ) {
            bakeChildFiles(filePathList, targetDir, dirBakerConf);
        }

        List<Path> dirPathList =
                entries.stream()
                .filter(Files::isDirectory)
                .collect(Collectors.toList());

        if ( dirPathList.size() > 0 ) {
            bakeChildDirectories(sourceDir, dirPathList, targetDir, dirBakerConf);
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
                .map(_templateService::getTemplate);
        if ( !template.isPresent() ) {
            template = _templateService.getDefaultTemplate();
        }

        for ( Path path : filePathList ) {
            _fileBaker.bake(path, template, targetDir);
        }
    }


    /**
     *
     */
    private void bakeChildDirectories(
            final Path sourceDir,
            final List<Path> dirPathList,
            final Path targetDir,
            final DirBakerConf parentConf) {

        DirBakerConf myBakerConf = buildDirBakerConf(parentConf, sourceDir);

        for ( Path childSourceDir : dirPathList ) {
            Path dirBasename = childSourceDir.getFileName();
            Path childTargetDir = targetDir.resolve(dirBasename);
            bakeDirectory(childSourceDir, childTargetDir, myBakerConf);
        }
    }


    /**
     * Builds the baker configuration for the given directory,
     * inheriting the values from the given parent baker conf.
     *
     * @param parentConf The configuration from which we will inherit
     * values.
     *
     * @param sourceDir The directory for which we are going to create
     * the baker conf.
     */
    private DirBakerConf buildDirBakerConf(
            final DirBakerConf parentConf,
            final Path sourceDir) {

        // TBD
        return parentConf;
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
