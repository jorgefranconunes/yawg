/**************************************************************************
 *
 * Copyright (c) 2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.logging.LogWithUtils;
import com.varmateo.yawg.util.Exceptions;


/**
 * Responsible for copying assets to the target directory.
 */
/* package private */ final class AssetsCopier {


    private final LogWithUtils _log;
    private final Optional<Path> _assetsDir;
    private final Path _targetDir;


    /**
     * @param conf All the parameters needed for performing a bake.
     */
    AssetsCopier(
            final LogWithUtils log,
            final Optional<Path> assetsDir,
            final Path targetDir) {

        _log = log;
        _assetsDir = assetsDir;
        _targetDir = targetDir;
    }


    /**
     *
     */
    public void copy()
            throws YawgException {

        if ( _assetsDir.isPresent() ) {
            _log.logDelay(
                    "copying assets",
                    () -> doCopy(_assetsDir.get(), _targetDir));
        } else {
            _log.debug("No assets directory to copy");
        }
    }


    /**
     *
     */
    private static void doCopy(
            final Path sourceDir,
            final Path targetDir)
            throws YawgException {

        try {
            copyDirectoryTree(sourceDir, targetDir);
        } catch ( IOException e ) {
            Exceptions.raise(
                    e,
                    "Failed to copy assets - {0} - {1}",
                    e.getClass().getSimpleName(),
                    e.getMessage());
        }
    }


    /**
     *
     */
    private static void copyDirectoryTree(
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