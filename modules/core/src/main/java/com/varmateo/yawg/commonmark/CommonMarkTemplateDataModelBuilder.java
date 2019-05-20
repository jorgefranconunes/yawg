/**************************************************************************
 *
 * Copyright (c) 2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.commonmark;

import java.io.IOException;
import java.nio.file.Path;

import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.TemplateDataModel;
import com.varmateo.yawg.spi.TemplateDataModelBuilder;
import com.varmateo.yawg.util.FileUtils;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;


/**
 *
 */
/* default */ final class CommonMarkTemplateDataModelBuilder {


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
        final String title = FileUtils.basename(sourcePath);
        final String pageUrl = context.dirUrl() + "/" + targetPath.getFileName();

        return TemplateDataModelBuilder.create()
                .setTitle(title)
                .setBody(body)
                .setPageUrl(pageUrl)
                .setRootRelativeUrl(context.rootRelativeUrl())
                .setPageVars(context.pageVars())
                .build();
    }


}
