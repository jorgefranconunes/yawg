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

import com.varmateo.commons.logging.LogWithUtils;

import com.varmateo.yawg.ItemBaker;
import com.varmateo.yawg.YawgBakerConf;
import com.varmateo.yawg.YawgDomain;
import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.YawgInfo;
import com.varmateo.yawg.YawgTemplate;
import com.varmateo.yawg.YawgTemplateService;


/**
 * Copies the contents of a source directory tree to a target
 * directory, transforming some files on the way.
 */
public final class YawgBaker
    extends Object {


    private final YawgBakerConf _conf;
    private final YawgDomain _domain;


    /**
     * @param conf All the parameters needed for performing a bake.
     */
    public YawgBaker(final YawgBakerConf conf) {

        _conf = conf;
        _domain = new YawgDomain(conf);
    }


    /**
     * Performs the baking of one directory tree into another.
     *
     * @exception YawgException Thrown if the baking could not be
     * completed for whatever reason.
     */
    public void bake()
            throws YawgException {

        LogWithUtils log = LogWithUtils.from(_domain.getLog());
        Path sourceDir = _conf.sourceDir;
        Path targetDir = _conf.targetDir;
        Path templatesDir = _conf.templatesDir;

        log.info("{0} {1}", YawgInfo.PRODUCT_NAME, YawgInfo.VERSION);
        log.info("    Source    : {0}", sourceDir);
        log.info("    Target    : {0}", targetDir);
        log.info(
                "    Templates : {0}",
                (templatesDir==null) ? "NONE" : templatesDir.toString());

        log.logDelay("bake", this::doBake);
    }


    /**
     *
     */
    private void doBake()
            throws YawgException {

        ItemBaker fileBaker = _domain.getFileBaker();
        Path sourceDir = _conf.sourceDir;
        Path targetDir = _conf.targetDir;

        try {
            bakeDirectory(fileBaker, sourceDir, targetDir);
        } catch ( IOException e ) {
            YawgException.raise(
                    e,
                    "Failed baking {0} - {1} - {2}",
                    sourceDir,
                    e.getClass().getSimpleName(),
                    e.getMessage());
        }
    }


    /**
     *
     */
    private void bakeDirectory(
            final ItemBaker fileBaker,
            final Path sourceDir,
            final Path targetDir)
            throws IOException, YawgException {

        if ( !Files.exists(targetDir) ) {
            Files.createDirectory(targetDir);
        }

        YawgTemplateService templateService = _domain.getTemplateService();
        Optional<YawgTemplate> template = templateService.getDefaultTemplate();
        List<Path> entries = getDirEntries(sourceDir);

        for ( Path path : entries ) {
            if ( Files.isRegularFile(path) ) {
                fileBaker.bake(path, template, targetDir);
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
