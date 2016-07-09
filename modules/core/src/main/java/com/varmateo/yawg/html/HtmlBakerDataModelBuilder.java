/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.TemplateDataModel;
import com.varmateo.yawg.util.FileUtils;


/**
 * Creates a <code>TemplateDataModel</code> from an input HTML
 * file.
 */
/* package private */ final class HtmlBakerDataModelBuilder
        extends Object {


    /**
     *
     */
    public HtmlBakerDataModelBuilder() {
        // Nothing to do.
    }


    /**
     *
     */
    public TemplateDataModel build(
            final Path sourcePath,
            final PageContext context)
            throws IOException {

        Optional<Document> document =
                Optional.of(Jsoup.parse(sourcePath.toFile(), null));
        String body =
                document
                .map(doc -> doc.getElementsByTag("body"))
                .flatMap(elems -> Optional.ofNullable(elems.first()))
                .map(Element::html)
                .orElse("");
        String title =
                document
                .map(doc -> doc.getElementsByTag("title"))
                .flatMap(elems -> Optional.ofNullable(elems.first()))
                .map(Element::text)
                .orElseGet(() -> FileUtils.basename(sourcePath));

        TemplateDataModel result =
                new TemplateDataModel.Builder()
                .setTitle(title)
                .setBody(body)
                .setRootRelativeUrl(context.rootRelativeUrl)
                .setTemplateVars(context.templateVars)
                .build();

        return result;
    }


}
