/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.asciidoctor.ast.DocumentHeader;
import org.asciidoctor.internal.AsciidoctorCoreException;

import com.varmateo.yawg.PageTemplateDataModel;
import com.varmateo.yawg.util.FileUtils;


/**
 *
 */
/* package private */ final class AsciidoctorBakerDataModelBuilder
        extends Object {


    private final Path _sourceRootDir;
    private final Asciidoctor _asciidoctor;


    /**
     *
     */
    public AsciidoctorBakerDataModelBuilder(
            final Path sourceRootDir,
            final Asciidoctor asciidoctor) {

        _sourceRootDir = sourceRootDir;
        _asciidoctor = asciidoctor;
    }


    /**
     *
     */
    public PageTemplateDataModel build(final Path sourcePath)
            throws AsciidoctorCoreException, IOException {

        String sourceContent = FileUtils.readAsString(sourcePath);
        OptionsBuilder options =
                OptionsBuilder.options()
                .headerFooter(false)
                .safe(SafeMode.UNSAFE);
        String body = _asciidoctor.render(sourceContent, options);
        DocumentHeader header = _asciidoctor.readDocumentHeader(sourceContent);
        String title =
                Optional.ofNullable(header)
                .map(h -> h.getDocumentTitle())
                .map(t -> t.getMain())
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
