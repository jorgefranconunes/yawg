/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
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
import com.varmateo.yawg.PageTemplateDataModel;
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

        PageTemplateDataModel model =
                buildModel("DocumentWithTitle.adoc", ".");

        assertEquals("Document with Title", model.title);
        assertTrue(model.body.contains(
                "The body of the document with a title."));
        assertEquals(".", model.rootRelativeUrl);
    }


    /**
     *
     */
    @Test
    public void withoutTitle()
            throws IOException {

        PageTemplateDataModel model =
                buildModel("DocumentWithoutTitle.adoc", ".");

        assertEquals("DocumentWithoutTitle", model.title);
        assertTrue(model.body.contains(
                "The body of the document without a title."));
        assertEquals(".", model.rootRelativeUrl);
    }


    /**
     *
     */
    private PageTemplateDataModel buildModel(
            final String relPath,
            final String rootRelativeUrl)
            throws IOException {

        Path sourcePath =
                TestUtils.getPath(
                        AsciidoctorBakerDataModelBuilder.class,
                        relPath);
        PageContext context =
                new PageContext.Builder()
                .setRootRelativeUrl(rootRelativeUrl)
                .build();
        PageTemplateDataModel model =
                _modelBuilder.build(
                        sourcePath,
                        sourcePath.getParent(),
                        context);

        return model;
    }


}
