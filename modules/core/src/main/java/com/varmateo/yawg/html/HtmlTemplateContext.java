/**************************************************************************
 *
 * Copyright (c) 2016-2020 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

import io.vavr.control.Try;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.TemplateContext;
import com.varmateo.yawg.spi.TemplateContextBuilder;
import com.varmateo.yawg.util.FileUtils;


/**
 * Creates a <code>TemplateContext</code> from an input HTML
 * file.
 */
final class HtmlTemplateContext {


    private static final String TAG_BODY = "body";
    private static final String TAG_TITLE = "title";


    private HtmlTemplateContext() {
        // Nothing to do.
    }


    /**
     *
     */
    public static Try<TemplateContext> create(
            final Path sourcePath,
            final Path targetPath,
            final PageContext context) {

        return parse(sourcePath)
                .map(buildTemplateContext(sourcePath, targetPath, context));
    }


    private static Try<Document> parse(final Path sourcePath) {

        return Try.of(() -> Jsoup.parse(sourcePath.toFile(), null))
                .recoverWith(HtmlPageBakerException.parseFailureTry(sourcePath));
    }


    private static Function<Document, TemplateContext> buildTemplateContext(
            final Path sourcePath,
            final Path targetPath,
            final PageContext context) {

        return (Document document) -> buildTemplateContext(
                sourcePath, targetPath, context, document);
    }


    private static TemplateContext buildTemplateContext(
            final Path sourcePath,
            final Path targetPath,
            final PageContext context,
            final Document document) {

        final String body = Optional.ofNullable(document.getElementsByTag(TAG_BODY).first())
                .map(Element::html)
                .orElse("");
        final String pageUrl = context.dirUrl() + "/" + targetPath.getFileName();
        final String title = Optional.ofNullable(document.getElementsByTag(TAG_TITLE).first())
                .map(Element::text)
                .orElseGet(() -> FileUtils.basename(sourcePath));

        return TemplateContextBuilder.create()
                .title(title)
                .body(body)
                .pageUrl(pageUrl)
                .rootRelativeUrl(context.rootRelativeUrl())
                .pageVars(context.pageVars())
                .bakeId(context.bakeId())
                .build();
    }


}
