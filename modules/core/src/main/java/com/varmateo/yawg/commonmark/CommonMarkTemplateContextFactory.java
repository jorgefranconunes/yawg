/**************************************************************************
 *
 * Copyright (c) 2019-2026 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.commonmark;

import java.nio.file.Path;
import java.util.function.Function;
import java.util.Optional;

import io.vavr.control.Try;
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.TemplateContext;
import com.varmateo.yawg.spi.TemplateContextBuilder;
import com.varmateo.yawg.util.FileUtils;

/* package private */ final class CommonMarkTemplateContextFactory {

    private static final String KEY_TITLE = "title";

    private final Parser _markdownParser;
    private final HtmlRenderer _htmlRenderer;

    public CommonMarkTemplateContextFactory(
            final Parser markdownParser,
            final HtmlRenderer htmlRenderer
    ) {
        _markdownParser = markdownParser;
        _htmlRenderer = htmlRenderer;
    }

    public Try<TemplateContext> build(
            final Path sourcePath,
            final Path targetDir,
            final Path targetPath,
            final PageContext context
    ) {
        return parse(sourcePath)
                .map(buildTemplateContext(sourcePath, targetPath, context));
    }

    private Try<Node> parse(final Path sourcePath) {
        return FileUtils.safeReadFrom(sourcePath, _markdownParser::parseReader)
                .recoverWith(CommonMarkPageBakerException.parseFailureTry(sourcePath));
    }

    private Function<Node, TemplateContext> buildTemplateContext(
            final Path sourcePath,
            final Path targetPath,
            final PageContext context
    ) {
        return (Node document) -> buildTemplateContext(sourcePath, targetPath, context, document);
    }

    private TemplateContext buildTemplateContext(
            final Path sourcePath,
            final Path targetPath,
            final PageContext context,
            final Node document
    ) {
        final String title = documentTitle(sourcePath, document);
        final String body = _htmlRenderer.render(document);
        final String pageUrl = context.dirUrl() + "/" + targetPath.getFileName();

        return TemplateContextBuilder.create()
                .title(title)
                .body(body)
                .pageUrl(pageUrl)
                .rootRelativeUrl(context.rootRelativeUrl())
                .pageVars(context.pageVars())
                .bakeId(context.bakeId())
                .build();
    }

    private String documentTitle(
            final Path sourcePath,
            final Node document
    ) {
        return frontMatterTitle(document)
                .orElseGet(() -> FileUtils.basename(sourcePath));
    }

    private Optional<String> frontMatterTitle(final Node document) {
        final YamlFrontMatterVisitor frontMatterVisitor = new YamlFrontMatterVisitor();
        document.accept(frontMatterVisitor);

        return Optional.ofNullable(frontMatterVisitor.getData().get(KEY_TITLE))
                .filter(xs -> xs.size() > 0)
                .map(xs -> xs.get(0));
    }
}
