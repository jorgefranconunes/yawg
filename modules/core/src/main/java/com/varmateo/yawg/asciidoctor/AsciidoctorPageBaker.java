/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Pattern;

import io.vavr.Lazy;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.internal.AsciidoctorCoreException;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.asciidoctor.AsciidoctorBakerDataModelBuilder;
import com.varmateo.yawg.spi.PageBaker;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.Template;
import com.varmateo.yawg.spi.TemplateDataModel;
import com.varmateo.yawg.util.Exceptions;
import com.varmateo.yawg.util.FileUtils;


/**
 * A <code>Baker</code> that translates text files in Asciidoc format
 * into HTML files.
 */
public final class AsciidoctorPageBaker
        implements PageBaker {


    private static final String NAME = "asciidoc";

    private static final Pattern RE_ADOC = Pattern.compile(".*\\.adoc$");

    private static final String TARGET_EXTENSION = ".html";

    private final Lazy<Asciidoctor> _asciidoctor =
            Lazy.of(this::newAsciidoctor);

    private final Lazy<AsciidoctorBakerDataModelBuilder> _modelBuilder =
            Lazy.of(this::newAsciidoctorBakerDataModelBuilder);


    private AsciidoctorPageBaker() {
        // Nothing to do.
    }


    /**
     *
     */
    public static PageBaker create() {

        return new AsciidoctorPageBaker();
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

        return FileUtils.isNameMatch(path, RE_ADOC);
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
     * @param context Provides the template for generating the target
     * document. If no template is provided, then the default
     * AsciidoctorJ document generator will be used.
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
            doBake(sourcePath, context, targetDir);
        } catch ( AsciidoctorCoreException | IOException e ) {
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
            throws AsciidoctorCoreException, IOException {

        final Path targetPath = getTargetPath(sourcePath, targetDir);
        final Optional<Template> template = context.templateFor(sourcePath);

        if ( template.isPresent() ) {
            doBakeWithTemplate(
                    sourcePath,
                    context,
                    targetDir,
                    targetPath,
                    template.get());
        } else {
            doBakeWithoutTemplate(
                    sourcePath,
                    context,
                    targetDir,
                    targetPath);
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
     *
     */
    private void doBakeWithoutTemplate(
            final Path sourcePath,
            final PageContext context,
            final Path targetDir,
            final Path targetPath)
            throws AsciidoctorCoreException {

        final OptionsBuilder options = AdocUtils.buildOptionsForBakeWithoutTemplate(
                sourcePath,
                targetDir,
                targetPath,
                context.pageVars());
        final File sourceFile = sourcePath.toFile();

        _asciidoctor.get().convertFile(sourceFile, options);
    }


    /**
     *
     */
    private void doBakeWithTemplate(
            final Path sourcePath,
            final PageContext context,
            final Path targetDir,
            final Path targetPath,
            final Template template)
            throws AsciidoctorCoreException, IOException {

        final TemplateDataModel dataModel = _modelBuilder.get().build(
                sourcePath, targetDir, targetPath, context);

        FileUtils.newWriter(
                targetPath,
                writer -> template.process(dataModel, writer));
    }


    /**
     *
     */
    private Asciidoctor newAsciidoctor() {

        final Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        // The following is required for supporting PlantUML diagrams
        // (cf. http://asciidoctor.org/docs/asciidoctor-diagram/).
        asciidoctor.requireLibrary("asciidoctor-diagram/plantuml");

        return asciidoctor;
    }


    /**
     *
     */
    private AsciidoctorBakerDataModelBuilder newAsciidoctorBakerDataModelBuilder() {
        return new AsciidoctorBakerDataModelBuilder(_asciidoctor.get());
    }


}