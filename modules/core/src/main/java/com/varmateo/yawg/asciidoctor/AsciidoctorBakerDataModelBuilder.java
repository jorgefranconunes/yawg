/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.asciidoctor.ast.DocumentHeader;
import org.asciidoctor.internal.AsciidoctorCoreException;

import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.TemplateDataModel;

import com.varmateo.yawg.util.FileUtils;


/**
 *
 */
/* package private */ final class AsciidoctorBakerDataModelBuilder
        extends Object {


    private static final String ATTR_DOC_DIR = "docdir";

    // Asciidoctor attribute specifying the directory when images
    // generated by PlatUML will be created.
    private static final String ATTR_IMAGES_OUT_DIR = "imagesoutdir";

    private final Asciidoctor _asciidoctor;


    /**
     *
     */
    public AsciidoctorBakerDataModelBuilder(final Asciidoctor asciidoctor) {

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
        AttributesBuilder attributes =
                AttributesBuilder.attributes()
                .attribute(ATTR_DOC_DIR, sourcePath.getParent().toString())
                .attribute(ATTR_IMAGES_OUT_DIR, targetDir.toString());
        OptionsBuilder options =
                OptionsBuilder.options()
                .attributes(attributes)
                .headerFooter(false)
                .safe(SafeMode.UNSAFE);
        String body = _asciidoctor.render(sourceContent, options);
        String pageUrl = context.getDirUrl() + "/" + targetPath.getFileName();
        DocumentHeader header = _asciidoctor.readDocumentHeader(sourceContent);
        String title =
                Optional.ofNullable(header)
                .map(h -> h.getDocumentTitle())
                .map(t -> t.getMain())
                .orElseGet(() -> FileUtils.basename(sourcePath));

        TemplateDataModel result =
                TemplateDataModel.builder()
                .setTitle(title)
                .setBody(body)
                .setPageUrl(pageUrl)
                .setRootRelativeUrl(context.getRootRelativeUrl())
                .setPageVars(context.getPageVars())
                .build();

        return result;
    }


}
