/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.varmateo.yawg.PageTemplateDataModel;
import com.varmateo.yawg.util.FileUtils;


/**
 * Creates a <code>PageTemplateDataModel</code> from an input HTML
 * file.
 */
/* package private */ final class HtmlBakerDataModelBuilder
        extends Object {


    private final Path _sourceRootDir;


    /**
     *
     */
    public HtmlBakerDataModelBuilder(final Path sourceRootDir) {

        _sourceRootDir = sourceRootDir;
    }


    /**
     *
     */
    public PageTemplateDataModel build(final Path sourcePath)
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
        String rootRelativeUrl = buildRootRelativeUrl(sourcePath);

        PageTemplateDataModel result =
                new PageTemplateDataModel.Builder()
                .setTitle(title)
                .setBody(body)
                .setRootRelativeUrl(rootRelativeUrl)
                .build();

        return result;
    }


    /**
     *
     */
    private String buildRootRelativeUrl(final Path sourcePath) {

        Path relDir = sourcePath.getParent().relativize(_sourceRootDir);
        String result = relDir.toString().replace(File.separatorChar, '/');

        if ( result.length() == 0 ) {
            result = ".";
        }

        return result;
    }


}
