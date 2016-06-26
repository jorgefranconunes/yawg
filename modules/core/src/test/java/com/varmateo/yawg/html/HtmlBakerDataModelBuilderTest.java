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

import com.varmateo.yawg.PageTemplateDataModel;
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

        Path sourcePath =
                TestUtils.getPath(
                        HtmlBakerDataModelBuilder.class,
                        "DocumentWithTitle.html");
        PageTemplateDataModel model = _modelBuilder.build(sourcePath, ".");

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

        Path sourcePath =
                TestUtils.getPath(
                        HtmlBakerDataModelBuilder.class,
                        "DocumentWithoutTitle.html");
        PageTemplateDataModel model = _modelBuilder.build(sourcePath, ".");

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

        Path sourcePath =
                TestUtils.getPath(
                        HtmlBakerDataModelBuilder.class,
                        "depth01/DocumentWithTitleDepth01.html");
        PageTemplateDataModel model = _modelBuilder.build(sourcePath, "..");

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

        Path sourcePath =
                TestUtils.getPath(
                        HtmlBakerDataModelBuilder.class,
                        "depth01/depth02/DocumentWithTitleDepth02.html");
        PageTemplateDataModel model = _modelBuilder.build(sourcePath, "../..");

        assertEquals("Document with Title", model.title);
        assertEquals(
                "<p>The body of the document with a title.</p>",
                model.body);
        assertEquals("../..", model.rootRelativeUrl);
    }


}
