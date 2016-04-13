/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import com.varmateo.yawg.BakerConf;
import com.varmateo.yawg.DirBaker;
import com.varmateo.yawg.DirBakerConf;
import com.varmateo.yawg.ItemBaker;
import com.varmateo.yawg.PageTemplateService;
import com.varmateo.yawg.YawgDomain;
import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.YawgInfo;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.logging.LogWithUtils;


/**
 * Copies the contents of a source directory tree to a target
 * directory, transforming some files on the way.
 */
public final class Baker
    extends Object {


    private final BakerConf _conf;
    private final YawgDomain _domain;


    /**
     * @param conf All the parameters needed for performing a bake.
     */
    public Baker(final BakerConf conf) {

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
        Path assetsDir   = _conf.assetsDir;

        log.info("{0} {1}", YawgInfo.PRODUCT_NAME, YawgInfo.VERSION);
        log.info("    Source    : {0}", sourceDir);
        log.info("    Target    : {0}", targetDir);
        log.info(
                "    Templates : {0}",
                (templatesDir==null) ? "NONE" : templatesDir.toString());
        log.info(
                "    Assets    : {0}",
                (assetsDir==null) ? "NONE" : assetsDir.toString());

        log.logDelay("bake", () -> doBake(log));
    }


    /**
     *
     */
    private void doBake(final LogWithUtils log)
            throws YawgException {

        if ( _conf.assetsDir != null ) {
            log.logDelay("copying assets", () -> copyAssets(log));
        } else {
            log.debug("No assets directory given");
        }

        log.logDelay("baking source tree", () -> bakeSourceTree(log));
    }


    /**
     *
     */
    private void bakeSourceTree(final Log log)
            throws YawgException {

        Path sourceDir = _conf.sourceDir;
        Path targetDir = _conf.targetDir;
        ItemBaker fileBaker = _domain.getFileBaker();
        PageTemplateService templateService = _domain.getTemplateService();
        DirBaker dirBaker =
                new DirBaker(log, sourceDir, fileBaker, templateService);
        DirBakerConf dirBakerConf =
                new DirBakerConf.Builder()
                .build();

        dirBaker.bakeDirectory(sourceDir, targetDir, dirBakerConf);
    }


    /**
     *
     */
    private void copyAssets(final Log log)
            throws YawgException {

        final Path sourceDir = _conf.assetsDir;
        final Path targetDir = _conf.targetDir;

        try {
            copyDirectoryTree(sourceDir, targetDir);
        } catch ( IOException e ) {
            YawgException.raise(
                    e,
                    "Failed to copy assets - {0} - {1}",
                    e.getClass().getSimpleName(),
                    e.getMessage());
        }
    }


    /**
     *
     */
    private void copyDirectoryTree(
            final Path sourceDir,
            final Path targetDir)
            throws IOException {

        Files.walkFileTree(
                sourceDir,
                new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(
                            final Path dir,
                            final BasicFileAttributes attrs)
                            throws IOException {
                        Path childTargetDir =
                                targetDir.resolve(sourceDir.relativize(dir));
                        if ( !Files.exists(childTargetDir) ) {
                            Files.createDirectory(childTargetDir);
                        }
                        return FileVisitResult.CONTINUE;
                    }
                    @Override
                    public FileVisitResult visitFile(
                            final Path file,
                            final BasicFileAttributes attrs)
                            throws IOException {
                        Files.copy(
                                file,
                                targetDir.resolve(sourceDir.relativize(file)),
                                StandardCopyOption.REPLACE_EXISTING,
                                StandardCopyOption.COPY_ATTRIBUTES);
                        return FileVisitResult.CONTINUE;
                    }
                });
    }


}
