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


    private AsciidoctorBakerDataModelBuilder _modelBuilder = null;


    /**
     *
     */
    @Before
    public void setUp() {

        Path sourceDir =
                TestUtils.getInputsDir(AsciidoctorBakerDataModelBuilder.class);
        Asciidoctor asciidoctor =
                Asciidoctor.Factory.create();

        _modelBuilder =
                new AsciidoctorBakerDataModelBuilder(sourceDir, asciidoctor);
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
        PageTemplateDataModel model = _modelBuilder.build(sourcePath);

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

        Path sourcePath =
                TestUtils.getPath(
                        AsciidoctorBakerDataModelBuilder.class,
                        "DocumentWithoutTitle.adoc");
        PageTemplateDataModel model = _modelBuilder.build(sourcePath);

        assertEquals("DocumentWithoutTitle", model.title);
        assertTrue(model.body.contains(
                "The body of the document without a title."));
        assertEquals(".", model.rootRelativeUrl);
    }


}
