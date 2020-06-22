/**************************************************************************
 *
 * Copyright (c) 2019-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.commonmark;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

import io.vavr.Lazy;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.commonmark.Extension;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

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
 * A <code>Baker</code> that translates text files in Markdown format
 * into HTML files.
 */
public final class CommonMarkPageBaker
        implements PageBaker {


    private static final String NAME = "markdown";

    private static final Pattern RE_ADOC = Pattern.compile(".*\\.md$");

    private static final String TARGET_EXTENSION = ".html";

    private final Lazy<Parser> _markdownParser =
            Lazy.of(this::createMarkdownParser);

    private final Lazy<HtmlRenderer> _htmlRenderer =
            Lazy.of(this::createHtmlRenderer);

    private final Lazy<CommonMarkTemplateContextFactory> _templateContextFactory =
            Lazy.of(this::createTemplateContextFactory);


    private CommonMarkPageBaker() {
        // Nothing to do.
    }


    /**
     *
     */
    public static PageBaker create() {

        return new CommonMarkPageBaker();
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
     *   <li>.md</li>
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
            final Path targetPath) {

        return renderBody(sourcePath)
                .flatMap(body -> renderContentAndSave(sourcePath, targetPath, body));
    }


    private Try<String> renderBody(final Path sourcePath) {

        final Try<Node> document = FileUtils.safeReadFrom(
                sourcePath,
                reader -> _markdownParser.get().parseReader(reader));
        final Try<String> body = document
                .map(doc -> _htmlRenderer.get().render(doc));

        return body
                .recoverWith(CommonMarkPageBakerException.commonMarkFailureTry(sourcePath));
    }


    private Try<Void> renderContentAndSave(
            final Path sourcePath,
            final Path targetPath,
            final String body) {

        final String contentTemplate = ""
                + "<!DOCTYPE html>%n"
                + "<html><body>%s</body></html>";
        final String content = String.format(contentTemplate, body);
        final Try<Void> result = FileUtils.safeWriteTo(
                targetPath,
                writer -> writer.write(content));

        return result
                .recoverWith(CommonMarkPageBakerException.commonMarkFailureTry(sourcePath));
    }


    /**
     *
     */
    private Try<Void> doBakeWithTemplate(
            final Path sourcePath,
            final PageContext context,
            final Path targetDir,
            final Path targetPath,
            final Template template) {

        final Try<TemplateContext> templateContext = _templateContextFactory.get().build(
                sourcePath, targetDir, targetPath, context);

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
                    .recoverWith(CommonMarkPageBakerException.templateFailureTry(sourcePath))
                    .flatMap(Results::toTry);
        };
    }


    private Parser createMarkdownParser() {

        final Extension frontMatterExtension = YamlFrontMatterExtension.create();
        final List<Extension> extensions = List.of(frontMatterExtension);

        return Parser.builder()
                .extensions(extensions)
                .build();
    }


    private HtmlRenderer createHtmlRenderer() {

        return HtmlRenderer.builder().build();
    }


    private CommonMarkTemplateContextFactory createTemplateContextFactory() {

        return new CommonMarkTemplateContextFactory(_markdownParser.get(), _htmlRenderer.get());
    }


}
