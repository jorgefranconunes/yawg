/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import java.io.IOException;
import java.nio.file.Path;

import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.ast.Author;
import org.asciidoctor.ast.DocumentHeader;
import org.asciidoctor.internal.AsciidoctorCoreException;

import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.TemplateContext;
import com.varmateo.yawg.spi.TemplateContextBuilder;
import com.varmateo.yawg.util.FileUtils;


/**
 *
 */
/* package private */ final class AsciidoctorTemplateContext {


    private AsciidoctorTemplateContext() {
        // Nothing to do.
    }


    /**
     *
     */
    public static Try<TemplateContext> create(
            final Asciidoctor asciidoctor,
            final Path sourcePath,
            final Path targetDir,
            final Path targetPath,
            final PageContext context) {

        return Try.of(() -> doCreate(asciidoctor, sourcePath, targetDir, targetPath, context))
                .recoverWith(AsciidoctorPageBakerException.asciidocFailureTry(sourcePath));
    }


    private static TemplateContext doCreate(
            final Asciidoctor asciidoctor,
            final Path sourcePath,
            final Path targetDir,
            final Path targetPath,
            final PageContext context)
            throws AsciidoctorCoreException, IOException {

        final String sourceContent = FileUtils.readAsString(sourcePath);
        final OptionsBuilder options = AdocUtils.buildOptionsForBakeWithTemplate(
                sourcePath,
                targetDir,
                context.pageVars());
        final String body = asciidoctor.render(sourceContent, options);
        final String pageUrl = context.dirUrl() + "/" + targetPath.getFileName();
        final DocumentHeader header = asciidoctor.readDocumentHeader(sourceContent);
        final String title = Option.of(header)
                .flatMap(h -> Option.of(h.getDocumentTitle()))
                .flatMap(t -> Option.of(t.getMain()))
                .getOrElse(() -> FileUtils.basename(sourcePath));

        final TemplateContextBuilder templateContextBuilder =
                TemplateContextBuilder.create()
                .title(title)
                .body(body)
                .pageUrl(pageUrl)
                .rootRelativeUrl(context.rootRelativeUrl())
                .pageVars(context.pageVars())
                .bakeId(context.bakeId());
        buildAuthors(templateContextBuilder, header);

        return templateContextBuilder.build();
    }


    private static void buildAuthors(
            final TemplateContextBuilder templateContextBuilder,
            final DocumentHeader header) {

        final java.util.List<Author> authors = header.getAuthors();

        // This convoluted logic is needed because
        // DocumentHeader.getAuthor(), DocumentHeader.getAuthors() do
        // not behave consistently with each other.

        if ( !authors.isEmpty() ) {
            List.ofAll(authors).forEach(
                    a -> templateContextBuilder.addAuthor(a.getFullName(), a.getEmail()));
        } else {
            final Author singleAuthor = header.getAuthor();

            if ( (singleAuthor!=null) && (singleAuthor.getFullName()!=null) )  {
                templateContextBuilder.addAuthor(
                        singleAuthor.getFullName(),
                        singleAuthor.getEmail());
            }
        }
    }


}
