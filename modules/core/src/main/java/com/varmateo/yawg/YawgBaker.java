/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.varmateo.commons.logging.Log;
import com.varmateo.commons.logging.LogWithUtils;

import com.varmateo.yawg.FileBaker;
import com.varmateo.yawg.YawgBakeConf;
import com.varmateo.yawg.YawgException;


/**
 * Copies the contents of a source directory tree to a target
 * directory, transforming some files on the way.
 */
public final class YawgBaker
    extends Object {


    private final LogWithUtils _log;


    /**
     * @param log The logger that will be used for logging.
     */
    public YawgBaker(final Log log) {

        _log = LogWithUtils.from(log);
    }


    /**
     * Performs the baking of one directory tree into another.
     *
     * @param conf Data for performing the baking.
     *
     * @exception YawgException Thrown if the baking could not be
     * completed for whatever reason.
     */
    public void bake(final YawgBakeConf conf)
        throws YawgException {

        _log.logDelay("bake", () -> doBake(conf));
    }


    /**
     *
     */
    private void doBake(final YawgBakeConf conf)
        throws YawgException {

        _log.debug("    Source dir : {0}", conf.sourceDir);
        _log.debug("    Target dir : {0}", conf.targetDir);

        FileBaker fileBaker = buildBaker(conf.sourceDir);

        try {
            bakeDirectory(fileBaker, conf.sourceDir, conf.targetDir);
        } catch ( IOException e ) {
            YawgException.raise(e,
                                "Failed baking {0} - {1} - {2}",
                                conf.sourceDir,
                                e.getClass().getSimpleName(),
                                e.getMessage());
        }
    }


    /**
     *
     */
    private FileBaker buildBaker(final Path sourceDir) {

        ItemBaker defaultBaker = new CopyBaker();
        Collection<ItemBaker> bakers =
                Arrays.asList(
                        new AsciidocBaker()
                              );
        FileBaker baker = new FileBaker(_log, sourceDir, bakers, defaultBaker);

        return baker;
    }


    /**
     *
     */
    private void bakeDirectory(
            final FileBaker fileBaker,
            final Path sourceDir,
            final Path targetDir)
            throws IOException, YawgException {

        if ( !Files.exists(targetDir) ) {
            Files.createDirectory(targetDir);
        }

        List<Path> entries = getDirEntries(sourceDir);

        for ( Path path : entries ) {
            if ( Files.isRegularFile(path) ) {
                fileBaker.bake(path,targetDir);
            }
        }

        for ( Path path : entries ) {
            if ( Files.isDirectory(path) ) {
                Path dirBasename = path.getFileName();
                Path childTargetDir = targetDir.resolve(dirBasename);
                bakeDirectory(fileBaker, path, childTargetDir);
            }
        }
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


}