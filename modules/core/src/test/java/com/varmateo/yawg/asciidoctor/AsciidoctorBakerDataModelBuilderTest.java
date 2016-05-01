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

import com.varmateo.yawg.PageTemplateDataModel;
import com.varmateo.yawg.asciidoctor.AsciidoctorBakerDataModelBuilder;


/**
 *
 */
public final class AsciidoctorBakerDataModelBuilderTest
        extends Object {


    private Asciidoctor _asciidoctor = null;


    /**
     *
     */
    @Before
    public void setUp() {

        _asciidoctor = Asciidoctor.Factory.create();
    }


    /**
     *
     */
    @Test
    public void withTitle()
            throws IOException {

        Path sourcePath =
                TestUtils.getPath(
                        AsciidoctorBakerDataModelBuilder.class,
                        "DocumentWithTitle.adoc");
        PageTemplateDataModel model =
                new AsciidoctorBakerDataModelBuilder(_asciidoctor)
                .build(sourcePath);

        assertEquals("Document with Title", model.getTitle());
        assertTrue(model.getBody().contains(
                "The body of the document with a title."));
    }


    /**
     *
     */
    @Test
    public void withoutTitle()
            throws IOException {

        Path sourcePath =
                TestUtils.getPath(
                        AsciidoctorBakerDataModelBuilder.class,
                        "DocumentWithoutTitle.adoc");
        PageTemplateDataModel model =
                new AsciidoctorBakerDataModelBuilder(_asciidoctor)
                .build(sourcePath);

        assertEquals("DocumentWithoutTitle", model.getTitle());
        assertTrue(model.getBody().contains(
                "The body of the document without a title."));
    }


}
