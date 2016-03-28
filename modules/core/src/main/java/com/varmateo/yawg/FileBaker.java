/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.varmateo.commons.logging.Log;

import com.varmateo.yawg.YawgException;


/**
 * 
 */
/* package private */ final class FileBaker
    extends Object {


    private final Log _log;
    private final Path _sourceRootDir;


    /**
     * @param log The logger that will be used for logging.
     *
     * @param sourceRootDir The top level directory being baked. This
     * is only used to improve logging messages.
     */
    public FileBaker(
            final Log log,
            final Path sourceRootDir) {

        _log = log;
        _sourceRootDir = sourceRootDir;
    }


    /**
     * Bakes the given file into the specified target directory.
     *
     * <p>The target directory must already exist. Otherwise an
     * exception will be thrown.</p>
     *
     * @param sourcePath The file to be baked.
     *
     * @param targetDir The directory where the result of the bake
     * will be created.
     *
     * @exception YawgException Thrown if the baking could not be
     * completed for whatever reason.
     */
    public void bake(
            final Path sourcePath,
            final Path targetDir)
            throws YawgException {

        try {
            doBake(sourcePath, targetDir);
        } catch ( IOException e ) {
            YawgException.raise(e,
                                "Failed baking {0} - {1} - {2}",
                                sourcePath,
                                e.getClass().getSimpleName(),
                                e.getMessage());
        }
    }


    /**
     *
     */
    private void doBake(
            final Path sourcePath,
            final Path targetDir)
            throws IOException {

        Path sourceRelPath = _sourceRootDir.relativize(sourcePath);
        _log.debug("Baking {0}", sourceRelPath);

        Path sourceBasename = sourcePath.getFileName();
        Path targetPath = targetDir.resolve(sourceBasename);

        Files.copy(sourcePath,
                   targetPath,
                   StandardCopyOption.REPLACE_EXISTING,
                   StandardCopyOption.COPY_ATTRIBUTES);
    }


}