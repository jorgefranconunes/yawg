/**************************************************************************
 *
 * Copyright (c) 2016-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

import io.vavr.Lazy;
import io.vavr.control.Try;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;

import com.varmateo.yawg.api.Result;
import com.varmateo.yawg.spi.PageBakeResult;
import com.varmateo.yawg.spi.PageBaker;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.Template;
import com.varmateo.yawg.spi.TemplateContext;
import com.varmateo.yawg.util.FileUtils;
import com.varmateo.yawg.util.PageBakeResults;
import com.varmateo.yawg.util.Results;


/**
 * A <code>Baker</code> that translates text files in Asciidoc format
 * into HTML files.
 */
public final class AsciidoctorPageBaker
        implements PageBaker {


    private static final String NAME = "asciidoc";

    private static final Pattern RE_ADOC = Pattern.compile(".*\\.adoc$");

    private static final String TARGET_EXTENSION = ".html";

    private final Lazy<Asciidoctor> _asciidoctor = Lazy.of(this::newAsciidoctor);


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
     * @return A result signaling success of failure.
     */
    @Override
    public PageBakeResult bake(
            final Path sourcePath,
            final PageContext context,
            final Path targetDir) {

        final Try<Void> result = doBake(sourcePath, context, targetDir);

        return PageBakeResults.fromTry(result);
    }


    /**
     *
     */
    private Try<Void> doBake(
            final Path sourcePath,
            final PageContext context,
            final Path targetDir) {

        final Path targetPath = getTargetPath(sourcePath, targetDir);
        final Optional<Template> template = context.templateFor(sourcePath);
        final Try<Void> result;

        if ( template.isPresent() ) {
            result = doBakeWithTemplate(
                    sourcePath,
                    context,
                    targetDir,
                    targetPath,
                    template.get());
        } else {
            result = doBakeWithoutTemplate(
                    sourcePath,
                    context,
                    targetDir,
                    targetPath);
        }

        return result;
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
    private Try<Void> doBakeWithoutTemplate(
            final Path sourcePath,
            final PageContext context,
            final Path targetDir,
            final Path targetPath) {

        final OptionsBuilder options = AdocUtils.buildOptionsForBakeWithoutTemplate(
                sourcePath,
                targetDir,
                targetPath,
                context.pageVars());
        final File sourceFile = sourcePath.toFile();

        return Try.run(() -> _asciidoctor.get().convertFile(sourceFile, options))
                .recoverWith(AsciidoctorPageBakerException.asciidocFailureTry(sourcePath));
    }


    private Try<Void> doBakeWithTemplate(
            final Path sourcePath,
            final PageContext context,
            final Path targetDir,
            final Path targetPath,
            final Template template) {

        final Try<TemplateContext> templateContext = AsciidoctorTemplateContext.create(
                _asciidoctor.get(), sourcePath, targetDir, targetPath, context);

        return templateContext.flatMap(processTemplate(sourcePath, targetPath, template));
    }


    private Function<TemplateContext, Try<Void>> processTemplate(
            final Path sourcePath,
            final Path targetPath,
            final Template template) {

        return (TemplateContext templateContext) -> {
            final Try<Result<Void>> result = FileUtils.safeWriteWith(
                    targetPath,
                    writer -> template.process(templateContext, writer));

            return result
                    .recoverWith(AsciidoctorPageBakerException.templateFailureTry(sourcePath))
                    .flatMap(Results::toTry);
        };
    }


    private Asciidoctor newAsciidoctor() {

        final Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        // The following is required for supporting PlantUML diagrams
        // (cf. http://asciidoctor.org/docs/asciidoctor-diagram/).
        asciidoctor.requireLibrary("asciidoctor-diagram/plantuml");

        return asciidoctor;
    }


}
