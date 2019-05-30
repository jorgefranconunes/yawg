/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Pattern;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.spi.PageBaker;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.Template;
import com.varmateo.yawg.spi.TemplateContext;
import com.varmateo.yawg.util.Exceptions;
import com.varmateo.yawg.util.FileUtils;


/**
 * A <code>Baker</code> that transforms HTML files into other HTML
 * files.
 */
public final class HtmlPageBaker
        implements PageBaker {


    private static final String NAME = "html";

    private static final Pattern RE_HTML = Pattern.compile(".*\\.html$");

    private static final String TARGET_EXTENSION = ".html";


    private HtmlPageBaker() {
        // Nothing to do.
    }


    /**
     *
     */
    public static PageBaker create() {

        return new HtmlPageBaker();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String shortName() {

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

        return FileUtils.isNameMatch(path, RE_HTML);
    }


    /**
     * Converts the given text file in HTML format into another HTML
     * file processed through the template engine.
     *
     * <p>The target directory must already exist. Otherwise an
     * exception will be thrown.</p>
     *
     * @param sourcePath The file to be baked.
     *
     * @param context Provides the template for generating the target
     * document. If no template is provided, then the source document
     * is just copied to the target directory.
     *
     * @param targetDir The directory where the baked file will be
     * copied to.
     *
     * @exception YawgException Thrown if the baking failed for
     * whatever reason.
     */
    @Override
    public void bake(
            final Path sourcePath,
            final PageContext context,
            final Path targetDir)
            throws YawgException {

        try {
            doBake(sourcePath, context, targetDir);
        } catch ( IOException e ) {
            Exceptions.raise(
                    e,
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
            final PageContext context,
            final Path targetDir)
            throws IOException {

        final Path targetPath = getTargetPath(sourcePath, targetDir);
        final Optional<Template> template = context.templateFor(sourcePath);

        if ( template.isPresent() ) {
            doBakeWithTemplate(sourcePath, context, targetPath, template.get());
        } else {
            doBakeWithoutTemplate(sourcePath, targetPath);
        }
    }


    /**
     *
     */
    private Path getTargetPath(
            final Path sourcePath,
            final Path targetDir) {

        final String sourceBasename = FileUtils.basename(sourcePath);
        final String targetName = sourceBasename + TARGET_EXTENSION;

        return targetDir.resolve(targetName);
    }


    /**
     * Baking without a template just copies the source HTML file to
     * the target location.
     */
    private void doBakeWithoutTemplate(
            final Path sourcePath,
            final Path targetPath)
            throws IOException {

        FileUtils.copy(sourcePath, targetPath);
    }


    /**
     *
     */
    private void doBakeWithTemplate(
            final Path sourcePath,
            final PageContext context,
            final Path targetPath,
            final Template template)
            throws IOException {

        final TemplateContext templateContext = HtmlTemplateContext.create(
                sourcePath, targetPath, context);

        FileUtils.newWriter(
                targetPath,
                writer -> template.process(templateContext, writer));
    }


}
