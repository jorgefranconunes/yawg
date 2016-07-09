/**************************************************************************
 *
 * Copyright (c) 2016 Jorge Nunes, All Rights Reserved.
 *
 **************************************************************************/

package com.varmateo.yawg.html;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.TemplateDataModel;
import com.varmateo.yawg.html.HtmlBakerDataModelBuilder;


/**
 *
 */
public final class HtmlBakerDataModelBuilderTest
        extends Object {


    private HtmlBakerDataModelBuilder _modelBuilder = null;


    /**
     *
     */
    @Before
    public void setUp() {

        _modelBuilder = new HtmlBakerDataModelBuilder();
    }


    /**
     *
     */
    @Test
    public void withTitle()
            throws IOException {

        TemplateDataModel model =
                buildModel("DocumentWithTitle.html", ".");

        assertEquals("Document with Title", model.title);
        assertEquals(
                "<p>The body of the document with a title.</p>",
                model.body);
        assertEquals(".", model.rootRelativeUrl);
    }


    /**
     *
     */
    @Test
    public void withoutTitle()
            throws IOException {

        TemplateDataModel model =
                buildModel("DocumentWithoutTitle.html", ".");

        assertEquals("DocumentWithoutTitle", model.title);
        assertEquals(
                "<p>The body of the document without a title.</p>",
                model.body);
        assertEquals(".", model.rootRelativeUrl);
    }


    /**
     *
     */
    @Test
    public void depthOne()
            throws IOException {

        TemplateDataModel model =
                buildModel("depth01/DocumentWithTitleDepth01.html", "..");

        assertEquals("Document with Title", model.title);
        assertEquals(
                "<p>The body of the document with a title.</p>",
                model.body);
        assertEquals("..", model.rootRelativeUrl);
    }


    /**
     *
     */
    @Test
    public void depthTwo()
            throws IOException {

        TemplateDataModel model =
                buildModel(
                        "depth01/depth02/DocumentWithTitleDepth02.html",
                        "../..");

        assertEquals("Document with Title", model.title);
        assertEquals(
                "<p>The body of the document with a title.</p>",
                model.body);
        assertEquals("../..", model.rootRelativeUrl);
    }


    /**
     *
     */
    private TemplateDataModel buildModel(
            final String relPath,
            final String rootRelativeUrl)
            throws IOException {

        Path sourcePath =
                TestUtils.getPath(HtmlBakerDataModelBuilder.class, relPath);
        PageContext context =
                new PageContext.Builder()
                .setRootRelativeUrl(rootRelativeUrl)
                .build();
        TemplateDataModel model = _modelBuilder.build(sourcePath, context);

        return model;
    }


}
