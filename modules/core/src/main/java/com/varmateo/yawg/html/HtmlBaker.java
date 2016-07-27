/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Pattern;

import com.varmateo.yawg.Baker;
import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.Template;
import com.varmateo.yawg.TemplateDataModel;
import com.varmateo.yawg.YawgException;

import com.varmateo.yawg.html.HtmlBakerDataModelBuilder;
import com.varmateo.yawg.util.Exceptions;
import com.varmateo.yawg.util.FileUtils;


/**
 * A <code>Baker</code> that transforms HTML files into other HTML
 * files.
 */
public final class HtmlBaker
        extends Object
        implements Baker {


    private static final String NAME = "html";

    private static final Pattern RE_HTML = Pattern.compile(".*\\.html$");

    private static final String TARGET_EXTENSION = ".html";

    private final HtmlBakerDataModelBuilder _modelBuilder;


    /**
     * 
     */
    public HtmlBaker() {

        _modelBuilder = new HtmlBakerDataModelBuilder();
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

        Path targetPath = getTargetPath(sourcePath, targetDir);
        Optional<Template> template = context.getPageTemplate();

        if ( template.isPresent() ) {
            doBakeWithTemplate(sourcePath, context, targetPath);
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

        String sourceBasename = FileUtils.basename(sourcePath);
        String targetName = sourceBasename + TARGET_EXTENSION;
        Path targetPath = targetDir.resolve(targetName);

        return targetPath;
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
            final Path targetPath)
            throws IOException {

        TemplateDataModel dataModel =
                _modelBuilder.build(sourcePath, targetPath, context);
        Template template = context.getPageTemplate().get();

        FileUtils.newWriter(
                targetPath,
                writer -> template.process(dataModel, writer));
    }


}