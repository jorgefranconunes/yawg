/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import java.io.IOException;
import java.nio.file.Path;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.spi.PageBaker;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.util.FileUtils;


/**
 * A <code>Baker</code> that just copies the source file to the target
 * location.
 */
/* default */ final class CopyPageBaker
        implements PageBaker {


    private static final String NAME = "copy";


    /**
     * 
     */
    CopyPageBaker() {

        // Nothing to do.
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String shortName() {

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
            throw CopyPageBakerException.copyFailure(sourcePath, targetDir, e);
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
