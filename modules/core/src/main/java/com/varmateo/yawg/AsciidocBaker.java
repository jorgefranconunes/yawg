/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.io.File;
import java.nio.file.Path;
import java.util.regex.Pattern;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.asciidoctor.internal.AsciidoctorCoreException;

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

    private final Asciidoctor _converter;


    /**
     * 
     */
    public AsciidocBaker() {

        _converter = Asciidoctor.Factory.create();
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
    @Override
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
        } catch ( AsciidoctorCoreException e ) {
            YawgException.raise(e,
                                "Failed {0} on {1} - {2} - {3}",
                                NAME,
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
            throws AsciidoctorCoreException {

        Path sourceBasename = sourcePath.getFileName();
        Path targetPath = getTargetPath(sourcePath, targetDir);

        File sourceFile = sourcePath.toFile();
        File targetFile = targetPath.toFile();

        AttributesBuilder attributes =
                AttributesBuilder.attributes()
                .noFooter(true);
        OptionsBuilder options =
                OptionsBuilder.options()
                .attributes(attributes)
                .inPlace(false)
                .safe(SafeMode.UNSAFE)
                .toFile(targetFile);

        _converter.convertFile(sourceFile, options);
    }


    /**
     *
     */
    private Path getTargetPath(
            final Path sourcePath,
            final Path targetDir) {

        String sourceBasename = sourcePath.getFileName().toString();
        int extensionIndex = sourceBasename.lastIndexOf(".");
        String sourceBasenameNoExtension =
                (extensionIndex>-1)
                ? sourceBasename.substring(0, extensionIndex)
                : sourceBasename;
        String targetBasename = sourceBasenameNoExtension + ".html";
        Path targetPath = targetDir.resolve(targetBasename);

        return targetPath;
    }


}