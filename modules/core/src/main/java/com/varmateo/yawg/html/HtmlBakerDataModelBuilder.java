/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.TemplateDataModel;

import com.varmateo.yawg.util.FileUtils;


/**
 * Creates a <code>TemplateDataModel</code> from an input HTML
 * file.
 */
final class HtmlBakerDataModelBuilder {


    private static final String TAG_BODY = "body";
    private static final String TAG_TITLE = "title";


    /**
     *
     */
    HtmlBakerDataModelBuilder() {
        // Nothing to do.
    }


    /**
     *
     */
    public TemplateDataModel build(
            final Path sourcePath,
            final Path targetPath,
            final PageContext context)
            throws IOException {

        Optional<Document> document =
                Optional.of(Jsoup.parse(sourcePath.toFile(), null));
        String body =
                document
                .map(doc -> doc.getElementsByTag(TAG_BODY))
                .flatMap(elems -> Optional.ofNullable(elems.first()))
                .map(Element::html)
                .orElse("");
        String pageUrl = context.getDirUrl() + "/" + targetPath.getFileName();
        String title =
                document
                .map(doc -> doc.getElementsByTag(TAG_TITLE))
                .flatMap(elems -> Optional.ofNullable(elems.first()))
                .map(Element::text)
                .orElseGet(() -> FileUtils.basename(sourcePath));

        return TemplateDataModel.builder()
                .setTitle(title)
                .setBody(body)
                .setPageUrl(pageUrl)
                .setRootRelativeUrl(context.getRootRelativeUrl())
                .setPageVars(context.getPageVars())
                .build();
    }


}
