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
import java.util.regex.Pattern;

import com.varmateo.yawg.ItemBaker;
import com.varmateo.yawg.YawgException;


/**
 * An <code>ItemBaker</code> that translates text files in Asciidoc
 * format into HTML files.
 */
/* package private */ final class AsciidocBaker
        extends Object
        implements ItemBaker {


    private static final String NAME = "asciidoc";

    private static Pattern RE_ADOC = Pattern.compile(".*\\.adoc$");


    /**
     * 
     */
    public AsciidocBaker() {

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
     * Checks if the given file name has one of the known extensions.
     *
     * <p>The following extensions will be allowed:</p>
     *
     * <ul>
     *   <li>.adoc</li>
     * </ul>
     *
     * @return True if the given file name has one of the allowed
     * extensions.
     */
    public boolean isBakeable(final Path path) {

        String basename = path.getFileName().toString();
        boolean result = RE_ADOC.matcher(basename).matches();

        return result;
    }


    /**
     * Converts the given text file in Asciidoc format into an HTML
     * file.
     *
     * <p>The target directory must already exist. Otherwise an
     * exception will be thrown.</p>
     *
     * @param sourcePath The file to be baked.
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

        // THIS IS A DUMMY IMPLEMENTATION. It just copies the file to
        // the target directory.

        // TBD

        Path sourceBasename = sourcePath.getFileName();
        Path targetPath = targetDir.resolve(sourceBasename);

        Files.copy(sourcePath,
                   targetPath,
                   StandardCopyOption.REPLACE_EXISTING,
                   StandardCopyOption.COPY_ATTRIBUTES);
    }


}