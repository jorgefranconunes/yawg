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
import com.varmateo.yawg.spi.TemplateDataModel;
import com.varmateo.yawg.spi.TemplateDataModelBuilder;
import com.varmateo.yawg.util.FileUtils;


/**
 *
 */
/* default */ final class CommonMarkTemplateDataModelBuilder {


    private static final String KEY_TITLE = "title";

    private final Parser _markdownParser;
    private final HtmlRenderer _htmlRenderer;


    /**
     *
     */
    CommonMarkTemplateDataModelBuilder(
            final Parser markdownParser,
            final HtmlRenderer htmlRenderer) {

        _markdownParser = markdownParser;
        _htmlRenderer = htmlRenderer;
    }


    /**
     *
     */
    public TemplateDataModel build(
            final Path sourcePath,
            final Path targetDir,
            final Path targetPath,
            final PageContext context)
            throws IOException {

        final Node document = FileUtils.newReader(sourcePath, _markdownParser::parseReader);
        final String body = _htmlRenderer.render(document);
        final String title = documentTitle(sourcePath, document);
        final String pageUrl = context.dirUrl() + "/" + targetPath.getFileName();

        return TemplateDataModelBuilder.create()
                .setTitle(title)
                .setBody(body)
                .setPageUrl(pageUrl)
                .setRootRelativeUrl(context.rootRelativeUrl())
                .setPageVars(context.pageVars())
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
