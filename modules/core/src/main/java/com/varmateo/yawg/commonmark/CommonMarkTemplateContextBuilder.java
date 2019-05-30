/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.commonmark;

import java.io.IOException;
import java.nio.file.Path;

import io.vavr.control.Option;

import org.commonmark.ext.front.matter.YamlFrontMatterVisitor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.TemplateContext;
import com.varmateo.yawg.spi.TemplateContextBuilder;
import com.varmateo.yawg.util.FileUtils;


/**
 *
 */
/* default */ final class CommonMarkTemplateContextBuilder {


    private static final String KEY_TITLE = "title";

    private final Parser _markdownParser;
    private final HtmlRenderer _htmlRenderer;


    /**
     *
     */
    CommonMarkTemplateContextBuilder(
            final Parser markdownParser,
            final HtmlRenderer htmlRenderer) {

        _markdownParser = markdownParser;
        _htmlRenderer = htmlRenderer;
    }


    /**
     *
     */
    public TemplateContext build(
            final Path sourcePath,
            final Path targetDir,
            final Path targetPath,
            final PageContext context)
            throws IOException {

        final Node document = FileUtils.newReader(sourcePath, _markdownParser::parseReader);
        final String body = _htmlRenderer.render(document);
        final String title = documentTitle(sourcePath, document);
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
            final Node document) {

        return frontMatterTitle(document)
                .getOrElse(() -> FileUtils.basename(sourcePath));
    }


    private Option<String> frontMatterTitle(final Node document) {

        final YamlFrontMatterVisitor frontMatterVisitor = new YamlFrontMatterVisitor();
        document.accept(frontMatterVisitor);

        return Option.of(frontMatterVisitor.getData().get(KEY_TITLE))
                .filter(xs -> xs.size() > 0)
                .map(xs -> xs.get(0));
    }


}
