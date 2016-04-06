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
import java.util.Optional;

import com.varmateo.yawg.ItemBaker;
import com.varmateo.yawg.YawgException;
import com.varmateo.yawg.PageTemplate;


/**
 * An <code>ItemBaker</code> that just copies the source file to the
 * target location.
 */
/* package private */ final class CopyBaker
        extends Object
        implements ItemBaker {


    private static final String NAME = "copy";


    /**
     * 
     */
    public CopyBaker() {

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
     * Always returns true. This means this <code>ItemBaker</code> can
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
     * @param template Not used.
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
            final Optional<PageTemplate> template,
            final Path targetDir)
            throws YawgException {

        try {
            doBake(sourcePath, targetDir);
        } catch ( IOException e ) {
            YawgException.raise(e,
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

        Files.copy(sourcePath,
                   targetPath,
                   StandardCopyOption.REPLACE_EXISTING,
                   StandardCopyOption.COPY_ATTRIBUTES);
    }


}
