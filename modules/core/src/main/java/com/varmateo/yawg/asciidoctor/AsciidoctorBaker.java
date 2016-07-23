/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Pattern;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.asciidoctor.internal.AsciidoctorCoreException;

import com.varmateo.yawg.Baker;
import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.Template;
import com.varmateo.yawg.TemplateDataModel;
import com.varmateo.yawg.YawgException;

import com.varmateo.yawg.asciidoctor.AsciidoctorBakerDataModelBuilder;
import com.varmateo.yawg.util.Exceptions;
import com.varmateo.yawg.util.FileUtils;


/**
 * A <code>Baker</code> that translates text files in Asciidoc format
 * into HTML files.
 */
/* package private */ final class AsciidoctorBaker
        extends Object
        implements Baker {


    private static final String NAME = "asciidoc";

    private static final Pattern RE_ADOC = Pattern.compile(".*\\.adoc$");

    private static final String TARGET_EXTENSION = ".html";

    private final Asciidoctor _asciidoctor;
    private final AsciidoctorBakerDataModelBuilder _modelBuilder;


    /**
     * 
     */
    public AsciidoctorBaker() {

        Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        // The following is required for supporting PlantUML diagrams
        // (cf. http://asciidoctor.org/docs/asciidoctor-diagram/).
        asciidoctor.requireLibrary("asciidoctor-diagram/plantuml");

        _asciidoctor = asciidoctor;
        _modelBuilder = new AsciidoctorBakerDataModelBuilder(asciidoctor);
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

        Path targetPath = getTargetPath(sourcePath, targetDir);
        Optional<Template> template = context.pageTemplate;

        if ( template.isPresent() ) {
            doBakeWithTemplate(sourcePath, context, targetDir, targetPath);
        } else {
            doBakeWithoutTemplate(sourcePath, targetDir, targetPath);
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
     *
     */
    private void doBakeWithoutTemplate(
            final Path sourcePath,
            final Path targetDir,
            final Path targetPath)
            throws AsciidoctorCoreException {

        File sourceFile = sourcePath.toFile();
        File targetFile = targetPath.toFile();
        AttributesBuilder attributes =
                AttributesBuilder.attributes()
                .attribute("imagesoutdir", targetDir.toString())
                .noFooter(false);
        OptionsBuilder options =
                OptionsBuilder.options()
                .attributes(attributes)
                .toFile(targetFile)
                .safe(SafeMode.UNSAFE);

        _asciidoctor.convertFile(sourceFile, options);
    }


    /**
     *
     */
    private void doBakeWithTemplate(
            final Path sourcePath,
            final PageContext context,
            final Path targetDir,
            final Path targetPath)
            throws AsciidoctorCoreException, IOException {

        TemplateDataModel dataModel =
                _modelBuilder.build(sourcePath, targetDir, targetPath, context);
        Template template = context.pageTemplate.get();
        StringWriter buffer = new StringWriter();

        template.process(dataModel, buffer);

        String content = buffer.toString();

        FileUtils.newWriter(
                targetPath,
                writer -> writer.write(content));
    }


}
