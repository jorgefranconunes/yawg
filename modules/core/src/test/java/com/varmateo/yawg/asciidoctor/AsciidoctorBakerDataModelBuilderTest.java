/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.asciidoctor;

import java.io.IOException;
import java.nio.file.Path;

import org.asciidoctor.Asciidoctor;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.TemplateDataModel;

import com.varmateo.yawg.core.PageContextBuilder;
import com.varmateo.yawg.asciidoctor.AsciidoctorBakerDataModelBuilder;


/**
 *
 */
public final class AsciidoctorBakerDataModelBuilderTest
        extends Object {


    private AsciidoctorBakerDataModelBuilder _modelBuilder = null;


    /**
     *
     */
    @Before
    public void setUp() {

        Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        _modelBuilder = new AsciidoctorBakerDataModelBuilder(asciidoctor);
    }


    /**
     *
     */
    @Test
    public void withTitle()
            throws IOException {

        TemplateDataModel model = buildModel("DocumentWithTitle.adoc");

        assertEquals("Document with Title", model.title);
        assertTrue(model.body.contains(
                "The body of the document with a title."));
    }


    /**
     *
     */
    @Test
    public void withoutTitle()
            throws IOException {

        TemplateDataModel model = buildModel("DocumentWithoutTitle.adoc");

        assertEquals("DocumentWithoutTitle", model.title);
        assertTrue(model.body.contains(
                "The body of the document without a title."));
    }


    /**
     *
     */
    private TemplateDataModel buildModel(final String relPath)
            throws IOException {

        String dirUrl = ".";
        String rootRelativeUrl = ".";
        PageContext context =
                new PageContextBuilder()
                .setDirUrl(dirUrl)
                .setRootRelativeUrl(rootRelativeUrl)
                .build();
        Path sourcePath =
                TestUtils.getPath(
                        AsciidoctorBakerDataModelBuilder.class,
                        relPath);
        TemplateDataModel model =
                _modelBuilder.build(
                        sourcePath,
                        sourcePath.getParent(),
                        sourcePath,
                        context);

        return model;
    }


}
