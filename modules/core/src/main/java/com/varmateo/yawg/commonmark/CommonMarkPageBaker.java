/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.commonmark;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Pattern;

import io.vavr.Lazy;
import io.vavr.collection.List;
import org.commonmark.Extension;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import com.varmateo.yawg.api.YawgException;
import com.varmateo.yawg.spi.PageBaker;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.Template;
import com.varmateo.yawg.spi.TemplateContext;
import com.varmateo.yawg.util.FileUtils;


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

    private final Lazy<CommonMarkTemplateContextBuilder> _templateContextBuilder =
            Lazy.of(this::createTemplateContextBuilder);


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
        } catch ( IOException e ) {
            throw CommonMarkPageBakerException.bakeFailure(sourcePath, targetDir, e);
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
            throws IOException {

        final String body = renderBody(sourcePath);
        final String contentTemplate = ""
                + "<!DOCTYPE html>\n"
                + "<html><body>%s</body></html>";
        final String content = String.format(contentTemplate, body);

        FileUtils.newWriter(
                targetPath,
                writer -> writer.write(content));
    }


    private String renderBody(final Path sourcePath)
            throws IOException {

        final Node document = FileUtils.newReader(
                sourcePath,
                reader -> _markdownParser.get().parseReader(reader));

        return _htmlRenderer.get().render(document);
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
            throws IOException {

        final TemplateContext dataModel = _templateContextBuilder.get().build(
                sourcePath, targetDir, targetPath, context);

        FileUtils.newWriter(
                targetPath,
                writer -> template.process(dataModel, writer));
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


    private CommonMarkTemplateContextBuilder createTemplateContextBuilder() {

        return new CommonMarkTemplateContextBuilder(_markdownParser.get(), _htmlRenderer.get());
    }


}
