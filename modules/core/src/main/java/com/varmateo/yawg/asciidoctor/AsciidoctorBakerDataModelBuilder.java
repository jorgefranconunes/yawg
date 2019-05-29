/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.TemplateDataModel;
import com.varmateo.yawg.spi.TemplateDataModelBuilder;
import com.varmateo.yawg.util.FileUtils;
import io.vavr.collection.List;
import io.vavr.control.Option;
import java.io.IOException;
import java.nio.file.Path;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.ast.Author;
import org.asciidoctor.ast.DocumentHeader;
import org.asciidoctor.internal.AsciidoctorCoreException;


/**
 *
 */
/* package private */ final class AsciidoctorBakerDataModelBuilder {


    private final Asciidoctor _asciidoctor;


    /**
     *
     */
    AsciidoctorBakerDataModelBuilder(final Asciidoctor asciidoctor) {

        _asciidoctor = asciidoctor;
    }


    /**
     *
     */
    public TemplateDataModel build(
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
        final String body = _asciidoctor.render(sourceContent, options);
        final String pageUrl = context.dirUrl() + "/" + targetPath.getFileName();
        final DocumentHeader header = _asciidoctor.readDocumentHeader(sourceContent);
        final String title = Option.of(header)
                .flatMap(h -> Option.of(h.getDocumentTitle()))
                .flatMap(t -> Option.of(t.getMain()))
                .getOrElse(() -> FileUtils.basename(sourcePath));

        final TemplateDataModelBuilder modelBuilder =
                TemplateDataModelBuilder.create()
                .setTitle(title)
                .setBody(body)
                .setPageUrl(pageUrl)
                .setRootRelativeUrl(context.rootRelativeUrl())
                .setPageVars(context.pageVars())
                .bakeId(context.bakeId());
        buildAuthors(modelBuilder, header);

        return modelBuilder.build();
    }


    /**
     *
     */
    private void buildAuthors(
            final TemplateDataModelBuilder modelBuilder,
            final DocumentHeader header) {

        java.util.List<Author> authors = header.getAuthors();

        // This convoluted logic is needed because
        // DocumentHeader.getAuthor(), DocumentHeader.getAuthors() do
        // not behave consistently with each other.

        if ( !authors.isEmpty() ) {
            List.ofAll(authors).forEach(
                    a -> modelBuilder.addAuthor(a.getFullName(), a.getEmail()));
        } else {
            Author singleAuthor = header.getAuthor();

            if ( (singleAuthor!=null) && (singleAuthor.getFullName()!=null) )  {
                modelBuilder.addAuthor(
                        singleAuthor.getFullName(),
                        singleAuthor.getEmail());
            }
        }
    }


}
