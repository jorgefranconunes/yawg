/**************************************************************************
 *
 * Copyright (c) 2017-2019 Yawg project contributors.
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
import java.util.function.BiConsumer;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.logging.Log;
import com.varmateo.yawg.logging.LogFactory;
import com.varmateo.yawg.logging.Logs;
import com.varmateo.yawg.util.Exceptions;


/**
 * Responsible for copying assets to the target directory.
 */
/* package private */ final class AssetsCopier {


    private final BiConsumer<Path, Path> _copyAction;


    /**
     * @param conf All the parameters needed for performing a bake.
     */
    AssetsCopier(final Log log) {

        _copyAction = Logs.decorateWithLogDuration(
                log,
                (source, target) -> "copy assets",
                AssetsCopier::doCopy);
    }


    /**
     *
     */
    public static AssetsCopier create() {

        final Log log = LogFactory.createFor(AssetsCopier.class);

        return new AssetsCopier(log);
    }


    /**
     *
     */
    public void copy(
            final Path assetsDir,
            final Path targetDir)
            throws YawgException {

        _copyAction.accept(assetsDir, targetDir);
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
