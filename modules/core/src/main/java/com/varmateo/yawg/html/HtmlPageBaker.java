/**************************************************************************
 *
 * Copyright (c) 2016-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

import io.vavr.control.Try;

import com.varmateo.yawg.api.Result;
import com.varmateo.yawg.spi.PageBaker;
import com.varmateo.yawg.spi.PageBakeResult;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.Template;
import com.varmateo.yawg.spi.TemplateContext;
import com.varmateo.yawg.util.FileUtils;
import com.varmateo.yawg.util.PageBakeResults;
import com.varmateo.yawg.util.Results;


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

        if ( template.isPresent() ) {
            return doBakeWithTemplate(sourcePath, context, targetPath, template.get());
        } else {
            return doBakeWithoutTemplate(sourcePath, targetPath);
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
    private Try<Void> doBakeWithoutTemplate(
            final Path sourcePath,
            final Path targetPath) {

        return FileUtils.safeCopy(sourcePath, targetPath)
                .recoverWith(HtmlPageBakerException.copyFailureTry(sourcePath));
    }


    /**
     *
     */
    private Try<Void> doBakeWithTemplate(
            final Path sourcePath,
            final PageContext context,
            final Path targetPath,
            final Template template) {

        final Try<TemplateContext> templateContext = HtmlTemplateContext.create(
                sourcePath, targetPath, context);

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
                    .recoverWith(HtmlPageBakerException.templateFailureTry(sourcePath))
                    .flatMap(Results::toTry);
        };
    }


}
