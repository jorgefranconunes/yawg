/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.IOException;
import java.nio.file.Path;

import com.varmateo.yawg.Baker;
import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.YawgException;

import com.varmateo.yawg.util.Exceptions;
import com.varmateo.yawg.util.FileUtils;


/**
 * A <code>Baker</code> that just copies the source file to the target
 * location.
 */
/* package private */ final class CopyBaker
        implements Baker {


    private static final String NAME = "copy";


    /**
     * 
     */
    CopyBaker() {

        // Nothing to do.
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getShortName() {

        return NAME;
    }


    /**
     * Always returns true. This means this <code>Baker</code> can
     * bake all types of files.
     *
     * @return Always true.
     */
    public boolean isBakeable(final Path path) {

        return true;
    }


    /**
     * Copies the given file into the specified target directory.
     *
     * <p>The target directory must already exist. Otherwise an
     * exception will be thrown.</p>
     *
     * @param sourcePath The file to be baked.
     *
     * @param context Not used.
     *
     * @param targetDir The directory where the source file will be
     * copied to.
     *
     * @exception YawgException Thrown if the copying failed for
     * whatever reason.
     */
    @Override
    public void bake(
            final Path sourcePath,
            final PageContext context,
            final Path targetDir)
            throws YawgException {

        try {
            doBake(sourcePath, targetDir);
        } catch ( IOException e ) {
            Exceptions.raise(
                    e,
                    "Failed copying {0} - {1} - {2}",
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

        Path sourceBasename = sourcePath.getFileName();
        Path targetPath = targetDir.resolve(sourceBasename);

        FileUtils.copy(sourcePath, targetPath);
    }


}
