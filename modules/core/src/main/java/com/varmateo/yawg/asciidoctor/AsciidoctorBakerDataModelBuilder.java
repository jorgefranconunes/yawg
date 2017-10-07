/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import java.io.IOException;
import java.nio.file.Path;

import io.vavr.collection.List;
import io.vavr.control.Option;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.ast.Author;
import org.asciidoctor.ast.DocumentHeader;
import org.asciidoctor.internal.AsciidoctorCoreException;

import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.TemplateDataModel;
import com.varmateo.yawg.util.FileUtils;


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

        String sourceContent = FileUtils.readAsString(sourcePath);
        OptionsBuilder options = AdocUtils.buildOptionsForBakeWithTemplate(
                sourcePath,
                targetDir,
                context.getPageVars());
        String body = _asciidoctor.render(sourceContent, options);
        String pageUrl = context.getDirUrl() + "/" + targetPath.getFileName();
        DocumentHeader header = _asciidoctor.readDocumentHeader(sourceContent);
        String title = Option.of(header)
                .flatMap(h -> Option.of(h.getDocumentTitle()))
                .flatMap(t -> Option.of(t.getMain()))
                .getOrElse(() -> FileUtils.basename(sourcePath));

        TemplateDataModel.Builder modelBuilder =
                TemplateDataModel.builder()
                .setTitle(title)
                .setBody(body)
                .setPageUrl(pageUrl)
                .setRootRelativeUrl(context.getRootRelativeUrl())
                .setPageVars(context.getPageVars());
        buildAuthors(modelBuilder, header);

        return modelBuilder.build();
    }


    /**
     *
     */
    private void buildAuthors(
            final TemplateDataModel.Builder modelBuilder,
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
